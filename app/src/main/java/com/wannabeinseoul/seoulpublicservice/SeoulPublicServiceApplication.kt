package com.wannabeinseoul.seoulpublicservice

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.imageLoader
import coil.request.ImageRequest
import com.naver.maps.map.util.FusedLocationSource
import com.wannabeinseoul.seoulpublicservice.databases.entity.UserEntity
import com.wannabeinseoul.seoulpublicservice.di.AppContainer
import com.wannabeinseoul.seoulpublicservice.di.DefaultAppContainer
import com.wannabeinseoul.seoulpublicservice.seoul.Row
import com.wannabeinseoul.seoulpublicservice.util.RoomRowMapper
import com.wannabeinseoul.seoulpublicservice.util.parseColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

private const val JJTAG = "jj-앱클래스"

class SeoulPublicServiceApplication : Application() {

    /** AppContainer instance used by the rest of classes to obtain dependencies */
    private lateinit var _container: AppContainer
    val container get() = _container

    private val tempKeyRowsSavedTime = "tempKeyRowsSavedTime"  // 나중에 이름 바꿀거. key 전역으로 관리할수도.

    private var _rowList: List<Row> = emptyList()
    val rowList: List<Row> get() = _rowList

    private val _initialLoadingFinished: MutableLiveData<Boolean> = MutableLiveData(false)
    val initialLoadingFinished: LiveData<Boolean> get() = _initialLoadingFinished

//    var lastLocation: Location? = null  // FusedLocationSource 직접 쓰게 변경

    var fusedLocationSource: FusedLocationSource? = null

    // TODO: 일단 그냥 public으로 씀
    var userName: MutableLiveData<String?> = MutableLiveData()
    var userProfileImagePlaceholder: Drawable? = null
    var userProfileImageDrawable: MutableLiveData<Drawable?> = MutableLiveData<Drawable?>()
    var userProfileImageUrl: String? = null
    var userId: String? = null
    var userColor: Int = 0

    fun setUser(user: UserEntity) {
        userId = user.userId
        userColor = user.userColor?.parseColor() ?: 0
        userProfileImageUrl = user.userProfileImage
        userProfileImagePlaceholder =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_profile_image, theme)!!
                .apply { setTint(userColor) }
//        imageLoader.enqueue(
//            ImageRequest.Builder(this)
//                .data(R.drawable.ic_profile_image)
//                .target(
//                    onSuccess = { userProfileImagePlaceholder = it.apply { setTint(userColor) } },
//                )
//                .build()
//        )
        userName.postValue(user.userName)
        imageLoader.enqueue(
            ImageRequest.Builder(this)
//                .data(R.drawable.ic_door)
                .data(user.userProfileImage)
                .target(
                    onError = {
                        Log.w(JJTAG, "setUser imageLoader fail, url: ${user.userProfileImage}")
                    },
                    onSuccess = { userProfileImageDrawable.postValue(it) }
                )
                .build()
        )
    }

    override fun onCreate() {
        super.onCreate()
        _container = DefaultAppContainer(this) { rowList }

        CoroutineScope(Dispatchers.Default).launch {
            updateRowList()
            _initialLoadingFinished.postValue(true)
        }
    }

    private suspend fun updateRowList() {
        val cm: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val cap = cm.getNetworkCapabilities(cm.activeNetwork)
        if (cap == null ||
            (cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)).not()
        ) {
            Looper.prepare()
            Toast.makeText(
                this,
                "네트워크 연결이 불가능하므로 저장된 데이터로 표시됩니다.",
                Toast.LENGTH_LONG
            ).show()
            getFromDB()
            Log.d(
                JJTAG, "cm.getNetworkCapabilities(cm.activeNetwork): $cap\n" +
                        "DB에서 꺼냄(네트워크 불가): ${rowList.toString().take(255)}"
            )
            return
        }

        var isOld = true
        val rowsSavedTime = container.prefRepository.load(tempKeyRowsSavedTime).toLongOrNull()
        if (rowsSavedTime == null) {
            Log.w(
                JJTAG,
                "prefRepository.load(tempKeyRowsSavedTime).toLongOrNull() == null"
            )
        } else {
            val timeDiff = System.currentTimeMillis() - rowsSavedTime
            Log.d(JJTAG, "timeDiff: $timeDiff")
            isOld = timeDiff > 180_000L
        }

        coroutineScope {
            if (isOld) {
                getAndUpdateAll2000()
            } else {
                getFromDB()
                if (_rowList.isEmpty()) getAndUpdateAll2000()
            }
        }
    }

    private suspend fun getAndUpdateAll2000() {
        try {
            withTimeout(6_000L) {
                _rowList = container.seoulPublicRepository.getAllParallel()
//                _rowList = container.seoulPublicRepository.getAll2000()
                val reservationEntities = RoomRowMapper.mappingRowToRoom(_rowList)
                container.reservationRepository.deleteAll()
                container.reservationRepository.insertAll(reservationEntities)
                container.prefRepository
                    .save(tempKeyRowsSavedTime, System.currentTimeMillis().toString())
            }
        } catch (e: Throwable) {
            Log.e(JJTAG, "데이터 통신 에러: $e")
            Looper.prepare()
            Toast.makeText(
                this,
                "데이터를 받아오는 과정에서 문제가 발생했습니다. 저장된 데이터로 표시됩니다.",
                Toast.LENGTH_LONG
            ).show()
            getFromDB()
        }
    }

    private suspend fun getFromDB() {
        val reservationEntities = container.reservationRepository.getAll()
        _rowList = RoomRowMapper.mappingRoomToRow(reservationEntities)
    }

}
