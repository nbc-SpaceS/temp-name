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

    fun getData(svcID: String) {
        val job = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _serviceData.postValue(reservationRepository.getService(svcID))
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            job.join()
        }
    }


    fun activeButtons() {   // 더보기 버튼 / 전화,예약,즐겨찾기,공유 버튼 등 버튼의 관리

    }

    fun mapSetting() {      // 네이버 지도에 마커찍기, 지도 레이아웃의 중심점 지정하기 등

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