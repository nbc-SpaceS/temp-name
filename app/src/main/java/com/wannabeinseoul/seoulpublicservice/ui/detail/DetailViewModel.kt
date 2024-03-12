package com.wannabeinseoul.seoulpublicservice.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ReviewRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ServiceRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.UserBanRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.UserRepository
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.SavedPrefRepository
import com.wannabeinseoul.seoulpublicservice.ui.dialog.review.ReviewItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailViewModel(
    private val reservationRepository: ReservationRepository,
    private val idPrefRepository: IdPrefRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    private val serviceRepository: ServiceRepository,
    private val savedPrefRepository: SavedPrefRepository,
    private val userBanRepository: UserBanRepository
) : ViewModel() {
    private val _serviceData = MutableLiveData<ReservationEntity>()
    val serviceData: LiveData<ReservationEntity> get() = _serviceData

    // 닫기 이벤트
    private val _closeEvent = MutableLiveData<Boolean>()
    val closeEvent: LiveData<Boolean> get() = _closeEvent

    private val _myLocationCallback = MutableLiveData<Boolean>()
    val myLocationCallback:LiveData<Boolean> get() = _myLocationCallback

    private val _textState = MutableLiveData<Boolean>()
    val textState: LiveData<Boolean> get() = _textState

    private val _reviewUiState: MutableLiveData<List<ReviewItem>> = MutableLiveData()
    val reviewUiState: LiveData<List<ReviewItem>> get() = _reviewUiState

    private val _savedID: MutableLiveData<Boolean> = MutableLiveData()
    val savedID: LiveData<Boolean> get() = _savedID

    private val _favoriteChanged: MutableLiveData<Boolean> = MutableLiveData()
    val favoriteChanged: LiveData<Boolean> get() = _favoriteChanged

    fun getData(svcID: String) {
        viewModelScope.launch{
            val result = viewModelScope.async(Dispatchers.IO) { // runBlocking으로 사용해도 됨
                reservationRepository.getService(svcID)
            }.await()
            result.let {
                _serviceData.value = result
            }
        }
    }

    fun close(event: Boolean) {
        _closeEvent.value = event
    }

    fun myLocationCallbackEvent(event: Boolean) {
        _myLocationCallback.value = event
    }

    fun textOpened(event: Boolean) {
        _textState.value = event
    }

    fun savedID(id: String) {
        _savedID.value = savedPrefRepository.contains(id)
    }

    fun changeFavorite(id: String) {
        if(savedPrefRepository.contains(id)) {
            savedPrefRepository.remove(id)
            _favoriteChanged.value = false
        } else {
            savedPrefRepository.addSvcid(id)
            _favoriteChanged.value = true
        }
    }

    fun setReviews(svcId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = serviceRepository.getServiceReviews(svcId)
            val banList = userBanRepository.getBanList().toMutableList().apply {
                remove(idPrefRepository.load())
            }

            _reviewUiState.postValue(data.filter { it.userId !in banList })
        }
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SeoulPublicServiceApplication)
                val container = application.container
                DetailViewModel(
                    reservationRepository = container.reservationRepository,
                    idPrefRepository = container.idPrefRepository,
                    reviewRepository = container.reviewRepository,
                    userRepository = container.userRepository,
                    serviceRepository = container.serviceRepository,
                    savedPrefRepository = container.savedPrefRepository,
                    userBanRepository = container.userBanRepository
                )
            }
        }
    }
}