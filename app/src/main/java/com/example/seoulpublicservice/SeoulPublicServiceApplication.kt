package com.example.seoulpublicservice

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.seoulpublicservice.di.AppContainer
import com.example.seoulpublicservice.di.DefaultAppContainer
import com.example.seoulpublicservice.seoul.Row
import com.example.seoulpublicservice.util.RoomRowMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class SeoulPublicServiceApplication : Application() {

    /** AppContainer instance used by the rest of classes to obtain dependencies */
    private lateinit var _container: AppContainer
    val container get() = _container

    private val tempKeyRowsSavedTime = "tempKeyRowsSavedTime"  // 나중에 이름 바꿀거. key 전역으로 관리할수도.

    private var _rowList: List<Row> = emptyList()
    val rowList: List<Row> get() = _rowList

    private val _initialLoadingFinished: MutableLiveData<Boolean> = MutableLiveData(false)
    val initialLoadingFinished: LiveData<Boolean> get() = _initialLoadingFinished

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
                "jj-앱클래스", "cm.getNetworkCapabilities(cm.activeNetwork): $cap\n" +
                        "DB에서 꺼냄(네트워크 불가): ${rowList.toString().take(255)}"
            )
            return
        }

        var isOld = true
        val rowsSavedTime = container.prefRepository.load(tempKeyRowsSavedTime).toLongOrNull()
        if (rowsSavedTime == null) {
            Log.w(
                "jj-앱클래스",
                "prefRepository.load(tempKeyRowsSavedTime).toLongOrNull() == null"
            )
        } else {
            val timeDiff = System.currentTimeMillis() - rowsSavedTime
            Log.d("jj-앱클래스", "timeDiff: $timeDiff")
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
                _rowList = container.seoulPublicRepository.getAll2000()
                val reservationEntities = RoomRowMapper.mappingRowToRoom(_rowList)
                container.reservationRepository.deleteAll()
                container.reservationRepository.insertAll(reservationEntities)
                container.prefRepository
                    .save(tempKeyRowsSavedTime, System.currentTimeMillis().toString())
            }
        } catch (e: Throwable) {
            Log.e("jj-앱클래스", "데이터 통신 에러: $e")
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
