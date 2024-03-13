package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ServiceRepository
import com.wannabeinseoul.seoulpublicservice.pref.RecommendPrefRepository
import com.wannabeinseoul.seoulpublicservice.seoul.Row
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepository
import com.wannabeinseoul.seoulpublicservice.usecase.GetAll2000UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RecommendationViewModel(
    private val recommendPrefRepository: RecommendPrefRepository,
    private val getAll2000UseCase: GetAll2000UseCase,
    private val seoulPublicRepository: SeoulPublicRepository,
    private val reservationRepository: ReservationRepository,
    private val serviceRepository: ServiceRepository
) : ViewModel() {


    private val _recommendations = MutableLiveData<List<RecommendationAdapter.MultiView>>()
    val recommendations: LiveData<List<RecommendationAdapter.MultiView>> get() = _recommendations



    private val _recommendationListLivedataList = List(4) {
        MutableLiveData<List<RecommendationData>>()
    }

    val recommendationListLivedataList get() = List<LiveData<List<RecommendationData>>>(4) {
        _recommendationListLivedataList[it]
    }


    init {
        viewModelScope.launch {
            _recommendationListLivedataList[0].postValue(getQuery("송파구"))
        }
        viewModelScope.launch {
            _recommendationListLivedataList[1].postValue(getQuery("청소년"))
        }
        viewModelScope.launch {
            _recommendationListLivedataList[2].postValue(getQuery("장애인"))
        }
        viewModelScope.launch {
            _recommendationListLivedataList[3].postValue(getQuery("풋살"))
        }
    }

    private suspend fun getQuery(query: String): List<RecommendationData> {
        val reservationEntities = reservationRepository.searchText(query).take(5)
        val counts = serviceRepository.getServiceReviewsCount(reservationEntities.map { it.SVCID })
        return List(reservationEntities.size) {
            RecommendationData(
                payType = reservationEntities[it].PAYATNM,
                areaName = reservationEntities[it].AREANM,
                placeName = reservationEntities[it].PLACENM,
                svcstatnm = reservationEntities[it].SVCSTATNM,
                imageUrl = reservationEntities[it].IMGURL,
                svcid = reservationEntities[it].SVCID,
                usetgtinfo = reservationEntities[it].USETGTINFO,
                reviewCount = counts[it]
            )
        }
    }

    fun getList(query: String, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = reservationRepository.searchText(query)
//            val entity = reservationRepository.getFilter(emptyList(), listOf(query), emptyList(), emptyList())
            val count = serviceRepository.getServiceReviewsCount(entity.take(5).map { it.SVCID })

            val itemList = mutableListOf<RecommendationData>()

            val minSize = minOf(entity.size, count.size)

            require(minSize >= 5) { "entity 또는 count의 크기가 충분하지 않습니다." }

            for (i in 0 until minSize) {
                itemList.add(RecommendationData(
                    payType = entity[i].PAYATNM,
                    areaName = entity[i].AREANM,
                    placeName = entity[i].PLACENM,
                    svcstatnm = entity[i].SVCSTATNM,
                    imageUrl = entity[i].IMGURL,
                    svcid = entity[i].SVCID,
                    usetgtinfo = entity[i].USETGTINFO,
                    reviewCount = count[i]
                ))
            }

            _recommendationListLivedataList.forEach { it.postValue(itemList) }

//            when(position) {
//                0 -> _firstRecommendation.postValue(itemList)
//                1 -> _secondRecommendation.postValue(itemList)
//                2 -> _thirdRecommendation.postValue(itemList)
//                3 -> _forthRecommendation.postValue(itemList)
//            }
        }
    }

    fun fetchRecommendations() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rowList = getAll2000UseCase()
                val recommendations = rowList.convertToRecommendationDataList()
//                _recommendations.postValue(recommendations)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as SeoulPublicServiceApplication).container
                RecommendationViewModel(
                    seoulPublicRepository = container.seoulPublicRepository,
                    recommendPrefRepository = container.recommendPrefRepository,
                    getAll2000UseCase = container.getAll2000UseCase,
                    reservationRepository = container.reservationRepository,
                    serviceRepository = container.serviceRepository
                )
            }
        }
    }

    private fun isReservationAvailableAbsence(row: Row): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        val rcptbgndtMillis = row.rcptbgndt.toLongOrNull() ?: return false
        val rcptenddtMillis = row.rcptenddt.toLongOrNull() ?: return false

        return currentTimeMillis >= rcptbgndtMillis && currentTimeMillis <= rcptenddtMillis
    }
}

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
