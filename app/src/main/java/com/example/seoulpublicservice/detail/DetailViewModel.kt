package com.example.seoulpublicservice.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seoulpublicservice.databases.ReservationEntity
import com.example.seoulpublicservice.databases.ReservationRepository

class DetailViewModel(
    private val reservationRepository: ReservationRepository
) : ViewModel() {
    private val _serviceData = MutableLiveData<ReservationEntity>()
    val serviceData: LiveData<ReservationEntity> get() = _serviceData

    private val _closeEvent = MutableLiveData<Boolean>()
    val closeEvent: LiveData<Boolean> get() = _closeEvent

    fun getData(svcID: String) {
        _serviceData.value = reservationRepository.getService(svcID)
    }


    fun activeButtons() {   // 더보기 버튼 / 전화,예약,즐겨찾기,공유 버튼 등 버튼의 관리

    }

    fun mapSetting() {      // 네이버 지도에 마커찍기, 지도 레이아웃의 중심점 지정하기 등

    }

    fun close(event: Boolean) { // Dismiss
        _closeEvent.value = event
    }
}