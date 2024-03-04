package com.example.seoulpublicservice.detail

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.seoulpublicservice.SeoulPublicServiceApplication
import com.example.seoulpublicservice.databases.ReservationEntity
import com.example.seoulpublicservice.databases.ReservationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(
    private val reservationRepository: ReservationRepository
) : ViewModel() {
    private val _serviceData = MutableLiveData<ReservationEntity>()
    val serviceData: LiveData<ReservationEntity> get() = _serviceData

    // 닫기 이벤트
    private val _closeEvent = MutableLiveData<Boolean>()
    val closeEvent: LiveData<Boolean> get() = _closeEvent

//    fun getData(svcID: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            _serviceData.postValue(reservationRepository.getService(svcID))
//        }
//    }
    fun getData(svcID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = reservationRepository.getService(svcID)
            _serviceData.postValue(data)
        }
    }

    fun close(event: Boolean) { // Dismiss
        _closeEvent.value = event
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SeoulPublicServiceApplication)
                val container = application.container
                DetailViewModel(
                    reservationRepository = container.reservationRepository
                )
            }
        }
    }
}