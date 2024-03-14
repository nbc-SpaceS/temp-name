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
import com.wannabeinseoul.seoulpublicservice.databases.firestore.ServiceRepository
import com.wannabeinseoul.seoulpublicservice.pref.RecommendPrefRepository
import com.wannabeinseoul.seoulpublicservice.seoul.Row
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepository
import com.wannabeinseoul.seoulpublicservice.usecase.GetAll2000UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch


class RecommendationViewModel(
    private val recommendPrefRepository: RecommendPrefRepository,
    private val getAll2000UseCase: GetAll2000UseCase,
    private val seoulPublicRepository: SeoulPublicRepository,
    private val reservationRepository: ReservationRepository,
    private val serviceRepository: ServiceRepository
) : ViewModel() {

    private val _horizontalDataList = MutableLiveData<List<RecommendationHorizontalData>>()
    val horizontalDataList: LiveData<List<RecommendationHorizontalData>> get() = _horizontalDataList

    private val _multiViews = MutableLiveData<List<RecommendationAdapter.MultiView>>()
    val multiViews: LiveData<List<RecommendationAdapter.MultiView>> get() = _multiViews
    fun setMultiViews(list: List<RecommendationAdapter.MultiView>) {
        _multiViews.value = list
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val items = listOf(
                Pair("송파구", "송파구에 있는 추천 서비스"),
                Pair("청소년", "청소년을 위한 서비스"),
                Pair("장애인", "장애인을 위한 서비스"),
                Pair("풋살", "풋살에 관한 서비스"),
            )

            // 일단 제목부터 띄워놓기 위함. 이거 대신에 로딩 처리를 해야 할 듯.
            _horizontalDataList.postValue(
                items.map { RecommendationHorizontalData(it.second, emptyList()) })

            val queryResults = items.map { async { getQuery(it.first) } }.awaitAll()
            val recommendationHorizontalDataList =
                queryResults.mapIndexed { index, recommendationDataList ->
                    RecommendationHorizontalData(items[index].second, recommendationDataList)
                }
            _horizontalDataList.postValue(recommendationHorizontalDataList)
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

//            _recommendationListLivedataList.forEach { it.postValue(itemList) }

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
