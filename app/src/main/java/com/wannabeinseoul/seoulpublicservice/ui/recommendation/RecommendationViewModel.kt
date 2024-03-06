package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wannabeinseoul.seoulpublicservice.databases.ReservationDAO
import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.pref.RecommendPrefRepository
import com.wannabeinseoul.seoulpublicservice.seoul.Row
import com.wannabeinseoul.seoulpublicservice.usecase.GetAll2000UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecommendationViewModel {
}

//    private val reservationRepository: ReservationRepository,
//    private val recommendPrefRepository: RecommendPrefRepository,
//    private val reservationDAO: ReservationDAO,
//    private val getAll2000UseCase: GetAll2000UseCase
//) : ViewModel() {
//    private val _regionServices: MutableLiveData<List<SealedMulti>> = MutableLiveData()
//    val regionServices: LiveData<List<SealedMulti>>
//        get() = _regionServices
//
//    private val _teenagerServices: MutableLiveData<List<SealedMulti>> = MutableLiveData()
//    val teenagerServices: LiveData<List<SealedMulti>>
//        get() = _teenagerServices
//
//    private val _disabilityServices: MutableLiveData<List<SealedMulti>> = MutableLiveData()
//    val disabilityServices: LiveData<List<SealedMulti>>
//        get() = _disabilityServices
//
//    private val _nextWeekServices: MutableLiveData<List<SealedMulti>> = MutableLiveData()
//    val nextWeekServices: LiveData<List<SealedMulti>>
//        get() = _nextWeekServices
//
//    fun fetchData() {
//        viewModelScope.launch {
//            // 예약 서비스 가져오기
//            val regionReservations = withContext(Dispatchers.Default) {
//                reservationRepository.getReservationsWithBigType("구")
//            }
//            val disabilityReservations = withContext(Dispatchers.Default) {
//                reservationRepository.getReservationsWithSmallType("장애인 서비스")
//            }
//            val regionRows = recommendPrefRepository.convertToRow(regionReservations)
//            // 예약 서비스를 SealedMulti로 변환
//            val regionServices = convertToSealedMulti(regionReservations)
//            val disabilityServices = convertToSealedMulti(disabilityReservations)
//
//            // 다음 주에 이용 가능한 서비스 가져오기
//            val nextWeekServices = withContext(Dispatchers.Default) {
//                val reservations: List<ReservationEntity> = // 적절한 예약 데이터를 가져옴
//                    convertToSealedMulti(reservations)
//            }
//
//            // LiveData에 저장
//            _regionServices.postValue(regionServices)
//            _disabilityServices.postValue(disabilityServices)
//            _nextWeekServices.postValue(nextWeekServices)
//        }
//    }
//
//
//    private suspend fun convertToSealedMulti(reservations: List<ReservationEntity>): List<SealedMulti> {
//        // 현재 시간을 가져옴
//        val currentTimeMillis = System.currentTimeMillis()
//        // 현재 시간 기준으로 7일 후의 시간을 계산
//        val oneWeekLaterTimeMillis = currentTimeMillis + 7 * 24 * 60 * 60 * 1000
//
//        return withContext(Dispatchers.IO) {
//            // 예약 시작 일자가 현재 날짜 이후이고 7일 이내인 데이터 필터링하여 SealedMulti로 변환
//            reservations.filter { reservation ->
//                val startDateMillis = reservation.SVCOPNBGNDT.toLong()
//                startDateMillis >= currentTimeMillis && startDateMillis < oneWeekLaterTimeMillis
//            }.map { reservation ->
//                SealedMulti.Recommendation(
//                    payType = reservation.PAYATNM,
//                    areaName = reservation.AREANM,
//                    placeName = reservation.PLACENM,
//                    isReservationAvailable = reservation.SVCNM,
//                    imageUrl = reservation.IMGURL,
//                    serviceList = reservation.SVCSTATNM
//                )
//            }
//        }
//    }
//
//
//
//    private fun getRandomDistrict(): String {
//        val allDistricts = listOf(
//            "송파구", "강남구", "강동구", "관악구", "구로구", // 추가할 지역구 계속해서 여기에 추가
//            // ...
//        )
//        return allDistricts.random()
//    }
