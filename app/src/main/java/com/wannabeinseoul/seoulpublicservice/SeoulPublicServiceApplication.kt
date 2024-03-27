package com.wannabeinseoul.seoulpublicservice

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.imageLoader
import coil.request.ImageRequest
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.util.FusedLocationSource
import com.wannabeinseoul.seoulpublicservice.databases.entity.UserEntity
import com.wannabeinseoul.seoulpublicservice.di.AppContainer
import com.wannabeinseoul.seoulpublicservice.di.DefaultAppContainer
import com.wannabeinseoul.seoulpublicservice.util.parseColor

private const val JJTAG = "jj-앱클래스"

class SeoulPublicServiceApplication : Application() {

    /** AppContainer instance used by the rest of classes to obtain dependencies */
    private lateinit var _container: AppContainer
    val container get() = _container

    // TODO: 초기값 없애는 거 고려
    private val _initialLoadingFinished: MutableLiveData<Boolean> = MutableLiveData(false)
    val initialLoadingFinished: LiveData<Boolean> get() = _initialLoadingFinished

    var fusedLocationSource: FusedLocationSource? = null
    fun getLastLatLng() = fusedLocationSource?.lastLocation?.let { LatLng(it) }

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
        _container = DefaultAppContainer(this)

        container.loadAndUpdateSeoulDataUseCase.invoke {
            _initialLoadingFinished.postValue(true)
        }
    }

}
