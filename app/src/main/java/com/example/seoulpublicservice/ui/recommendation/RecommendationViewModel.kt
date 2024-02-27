package com.example.seoulpublicservice.ui.recommendation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seoulpublicservice.databases.ReservationDAO
import com.example.seoulpublicservice.databases.ReservationEntity
import com.example.seoulpublicservice.databases.ReservationRepository
import com.example.seoulpublicservice.seoul.Row
import com.example.seoulpublicservice.usecase.GetAll2000UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class RecommendationViewModel(
    private val reservationRepository: ReservationRepository,
    private val reservationDAO: ReservationDAO,
    private val getAll2000UseCase: GetAll2000UseCase) : ViewModel() {
    private val _items = MutableLiveData<List<Row>>()
    val items: LiveData<List<Row>> get() = _items

    private val _regionServices: MutableLiveData<List<ReservationEntity>> = MutableLiveData()
    val regionServices: LiveData<List<ReservationEntity>> = _regionServices

    private val _disabilityServices: MutableLiveData<List<ReservationEntity>> = MutableLiveData()
    val disabilityServices: LiveData<List<ReservationEntity>> = _disabilityServices

    private val _nextWeekServices: MutableLiveData<List<ReservationEntity>> = MutableLiveData()
    val nextWeekServices: LiveData<List<ReservationEntity>> = _nextWeekServices

    fun fetchData() {
        viewModelScope.launch(Dispatchers.Default) {
            val regionServices = reservationRepository.getReservationsWithBigType("구")
            val disabilityServices = reservationRepository.getReservationsWithSmallType("장애인 서비스")
            val nextWeekServices = getServicesAvailableNextWeek()

            viewModelScope.launch {
                // getAll2000UseCase를 호출하여 데이터를 가져옵니다.
                val useCaseItems : List<ReservationEntity> = getAll2000UseCase()

            }
            // 가져온 서비스 목록들을 LiveData에 저장
            _regionServices.postValue(regionServices)
            _disabilityServices.postValue(disabilityServices)
            _nextWeekServices.postValue(nextWeekServices)
        }
    }


    private suspend fun getServicesAvailableNextWeek(): List<ReservationEntity> {
        // 현재 시간을 가져옴
        val currentTimeMillis = System.currentTimeMillis()
        // 현재 시간 기준으로 7일 후의 시간을 계산
        val oneWeekLaterTimeMillis = currentTimeMillis + 7 * 24 * 60 * 60 * 1000

        // 모든 예약 정보를 가져옴
        val allReservations = reservationRepository.getAllReservations()

        // 예약 시작 일자가 현재 날짜 이후인 데이터 필터링
        return allReservations.filter { reservation ->
            val startDateMillis = reservation.SVCOPNBGNDT.toLong()
            startDateMillis >= currentTimeMillis && startDateMillis < oneWeekLaterTimeMillis
        }
    }
}