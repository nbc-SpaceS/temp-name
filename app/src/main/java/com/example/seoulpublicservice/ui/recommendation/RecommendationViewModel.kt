package com.example.seoulpublicservice.ui.recommendation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seoulpublicservice.databases.ReservationDAO
import com.example.seoulpublicservice.databases.ReservationEntity
import com.example.seoulpublicservice.databases.ReservationRepository
import com.example.seoulpublicservice.pref.RecommendPrefRepository
import com.example.seoulpublicservice.seoul.Row
import com.example.seoulpublicservice.usecase.GetAll2000UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecommendationViewModel(
    private val reservationRepository: ReservationRepository,
    private val recommendPrefRepository: RecommendPrefRepository,
    private val reservationDAO: ReservationDAO,
    private val getAll2000UseCase: GetAll2000UseCase

) : ViewModel() {
    private val _items = MutableLiveData<List<Row>>()
    val items: LiveData<List<Row>> get() = _items

    private val _regionServices: MutableLiveData<List<SealedMulti>> = MutableLiveData()
    val regionServices: LiveData<List<SealedMulti>>
        get() = _regionServices

    private val _disabilityServices: MutableLiveData<List<SealedMulti>> = MutableLiveData()
    val disabilityServices: LiveData<List<SealedMulti>>
        get() = _disabilityServices

    private val _nextWeekServices: MutableLiveData<List<SealedMulti>> = MutableLiveData()
    val nextWeekServices: LiveData<List<SealedMulti>>
        get() = _nextWeekServices

    fun fetchData() {
        viewModelScope.launch {
            // 예약 서비스 가져오기
            val regionReservations = withContext(Dispatchers.Default) {
                reservationRepository.getReservationsWithBigType("구")
            }
            val disabilityReservations = withContext(Dispatchers.Default) {
                reservationRepository.getReservationsWithSmallType("장애인 서비스")
            }

            // 예약 서비스를 Row로 변환
            val regionRows = recommendPrefRepository.convertToRow(regionReservations)
            val disabilityRows = recommendPrefRepository.convertToRow(disabilityReservations)

            // 예약 서비스를 SealedMulti로 변환
            val regionServices = convertToSealedMulti(regionRows)
            val disabilityServices = convertToSealedMulti(disabilityRows)

            // 다음 주에 이용 가능한 서비스 가져오기
            val nextWeekServices = withContext(Dispatchers.Default) {
                convertToSealedMulti()
            }

            // 모든 서비스 가져오기
            val rows = withContext(Dispatchers.Default) {
                getAll2000UseCase()
            }

            // SealedMulti로 변환
            val sealedMultiList = convertToSealedMulti(rows)

            // LiveData에 저장
            _regionServices.postValue(regionServices)
            _disabilityServices.postValue(disabilityServices)
            _nextWeekServices.postValue(nextWeekServices)
        }
    }
    private fun convertToSealedMulti(rows: List<Row>): List<SealedMulti> {
        return rows.map { row ->
            SealedMulti.Recommendation(
                payType = row.payatnm,
                areaName = row.areanm,
                placeName = row.placenm,
                isReservationAvailable = row.svcnm,
                imageUrl = row.imgurl,
                serviceList = row.service
            )
        }
    }

    private suspend fun convertToSealedMulti(): List<SealedMulti> {
        // 현재 시간을 가져옴
        val currentTimeMillis = System.currentTimeMillis()
        // 현재 시간 기준으로 7일 후의 시간을 계산
        val oneWeekLaterTimeMillis = currentTimeMillis + 7 * 24 * 60 * 60 * 1000

        return withContext(Dispatchers.IO) {
            // 모든 예약 정보를 가져옴
            val allReservations = reservationRepository.getAllReservations()

            // 예약 시작 일자가 현재 날짜 이후이고 7일 이내인 데이터 필터링하여 SealedMulti로 변환
            allReservations.filter { reservation ->
                val startDateMillis = reservation.SVCOPNBGNDT.toLong()
                startDateMillis >= currentTimeMillis && startDateMillis < oneWeekLaterTimeMillis
            }.map { reservation ->
                SealedMulti.Recommendation(
                    payType = reservation.PAYATNM,
                    areaName = reservation.AREANM,
                    placeName = reservation.PLACENM,
                    isReservationAvailable = reservation.SVCNM,
                    imageUrl = reservation.IMGURL,
                    serviceList = reservation.SVCSTATNM
                )
            }
        }
    }
}
