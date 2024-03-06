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

    private val _myLocationCallback = MutableLiveData<Boolean>()
    val myLocationCallback:LiveData<Boolean> get() = _myLocationCallback

    private val _textState = MutableLiveData<Boolean>()
    val textState: LiveData<Boolean> get() = _textState

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

    fun close(event: Boolean) {
        _closeEvent.value = event
    }

    fun myLocationCallbackEvent(event: Boolean) {
        _myLocationCallback.value = event
    }

    fun textOpened(event: Boolean) {
        _textState.value = event
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