package com.wannabeinseoul.seoulpublicservice.detail

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailViewModel(
    private val reservationRepository: ReservationRepository
) : ViewModel() {
    private val _serviceData = MutableLiveData<ReservationEntity>()
    val serviceData: LiveData<ReservationEntity> get() = _serviceData

    // 닫기 이벤트
    private val _closeEvent = MutableLiveData<Boolean>()
    val closeEvent: LiveData<Boolean> get() = _closeEvent

    private val _callbackEvent = MutableLiveData<Boolean>()
    val callbackEvent:LiveData<Boolean> get() = _callbackEvent

    fun getData(svcID: String) {
        viewModelScope.launch{
            val result = viewModelScope.async(Dispatchers.IO) {
                reservationRepository.getService(svcID)
            }.await()
            result.let {
                _serviceData.value = result
            }
        }
    }

    fun close(event: Boolean) { // Dismiss
        _closeEvent.value = event
    }

    fun callbackEvent(event: Boolean) {
        _callbackEvent.value = event
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