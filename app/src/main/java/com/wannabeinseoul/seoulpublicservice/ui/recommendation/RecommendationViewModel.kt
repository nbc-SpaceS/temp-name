package com.wannabeinseoul.seoulpublicservice.ui.recommendation

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
import com.wannabeinseoul.seoulpublicservice.databases.firestore.ServiceRepository
import com.wannabeinseoul.seoulpublicservice.pref.RecommendPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch


class RecommendationViewModel(
    private val recommendPrefRepository: RecommendPrefRepository,
    private val seoulPublicRepository: SeoulPublicRepository,
    private val reservationRepository: ReservationRepository,
    private val serviceRepository: ServiceRepository,
    private val regionPrefRepository: RegionPrefRepository
) : ViewModel() {
    private inner class RandomQueryUseCase {
        private val entitiesMap = mapOf<String, List<ReservationEntity>>()

        operator fun invoke(query: String, num: Int): List<ReservationEntity> {
            // TODO: -ing


            return emptyList()
        }
    }

    private var isFirst: Boolean = true
    private var dataList = listOf<RecommendationHorizontalData>()

    private val _horizontalDataList = MutableLiveData<List<RecommendationHorizontalData>>()
    val horizontalDataList: LiveData<List<RecommendationHorizontalData>> get() = _horizontalDataList

    private val _multiViews = MutableLiveData<List<RecommendationAdapter.MultiView>>()
    val multiViews: LiveData<List<RecommendationAdapter.MultiView>> get() = _multiViews
    fun setMultiViews(list: List<RecommendationAdapter.MultiView>) {
        _multiViews.value = list
    }

    private val isLoading = MutableLiveData<Boolean>()

    init {
        isLoading.value = true // 로딩 상태로 초기화

        viewModelScope.launch(Dispatchers.IO) {
            val selectedRegions = regionPrefRepository.load() // 선택한 모든 지역을 가져옴
            val items = mutableListOf<Pair<String, String>>()
            val regionItems = mutableListOf<Pair<String, String>>()

            // 첫 번째 초기화 로직: 지역 외 로직
            items.add(Pair("교육", "교육과 관련된 서비스"))
            items.add(Pair("청소년", "청소년을 위한 서비스"))
            items.add(Pair("장애인", "장애인을 위한 서비스"))
            items.add(Pair("풋살", "풋살에 관한 서비스"))

            // 두 번째 초기화 로직: 다중 선택한 지역에 대한 로직
            selectedRegions.forEach { region ->
                val regionInfo = region + "에 관한 서비스"
                regionItems.add(Pair(region, regionInfo))
            }

            val queryResults = (items + regionItems).map { async { getQuery(it.first) } }.awaitAll()
            val recommendationHorizontalDataList =
                queryResults.mapIndexed { index, recommendationDataList ->
                    RecommendationHorizontalData(
                        (items + regionItems)[index].first,
                        (items + regionItems)[index].second,
                        recommendationDataList
                    )
                }

            dataList = recommendationHorizontalDataList
            _horizontalDataList.postValue(recommendationHorizontalDataList)
            isLoading.postValue(false)
            isFirst = false
        }
    }

    private suspend fun getQuery(query: String): List<RecommendationData> {
        val reservationEntities =
            reservationRepository.searchText(query).take(5)
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
                reviewCount = counts[it],
                serviceName = reservationEntities[it].SVCNM,
            )
        }
    }

    suspend fun getAdditionalQuery(query: String, num: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val searchText = reservationRepository.searchText(query)

            if (searchText.size >= num) {
                val reservationEntities =
                    searchText.slice(num - 5 until num)
                val counts =
                    serviceRepository.getServiceReviewsCount(reservationEntities.map { it.SVCID })

                val list = dataList.find { it.keyword == query }?.list ?: emptyList()

                multiViews.value?.map { view ->
                    if (view is RecommendationAdapter.MultiView.Horizontal) {
                        if (view.keyword == query) {
                            view.apply {
                                val list2 = List(reservationEntities.size) {
                                    RecommendationData(
                                        payType = reservationEntities[it].PAYATNM,
                                        areaName = reservationEntities[it].AREANM,
                                        placeName = reservationEntities[it].PLACENM,
                                        svcstatnm = reservationEntities[it].SVCSTATNM,
                                        imageUrl = reservationEntities[it].IMGURL,
                                        svcid = reservationEntities[it].SVCID,
                                        usetgtinfo = reservationEntities[it].USETGTINFO,
                                        reviewCount = counts[it],
                                        serviceName = reservationEntities[it].SVCNM,
                                    )
                                }
                                adapter.submitList(list + list2)

                                dataList = dataList.map { data ->
                                    if (data.keyword == query) {
                                        data.copy(list = data.list + list2)
                                    } else {
                                        data
                                    }
                                }
                            }
                        } else {
                            view
                        }
                    } else {
                        view
                    }
                }
            }
        }
    }

    suspend fun fetchRegionList() {
        if (!isFirst) {
            isLoading.postValue(true) // 로딩 상태로 초기화

            viewModelScope.launch(Dispatchers.IO) {
                val selectedRegions = regionPrefRepository.load() // 선택한 모든 지역을 가져옴

                val regionItems = mutableListOf<Pair<String, String>>()

                // 두 번째 초기화 로직: 다중 선택한 지역에 대한 로직
                selectedRegions.forEach { region ->
                    val regionInfo = region + "에 관한 서비스"
                    regionItems.add(Pair(region, regionInfo))
                }

                val queryResults = regionItems.map { async { getQuery(it.first) } }.awaitAll()
                val recommendationHorizontalDataList =
                    queryResults.mapIndexed { index, recommendationDataList ->
                        RecommendationHorizontalData(
                            regionItems[index].first,
                            regionItems[index].second,
                            recommendationDataList
                        )
                    }

                val updateDate = horizontalDataList.value.orEmpty().toMutableList()
                    .subList(0, 4) + recommendationHorizontalDataList
                dataList = updateDate
                _horizontalDataList.postValue(updateDate)
                isLoading.postValue(false)
            }
        }
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val container =
                    (this[APPLICATION_KEY] as SeoulPublicServiceApplication).container
                RecommendationViewModel(
                    recommendPrefRepository = container.recommendPrefRepository,
                    seoulPublicRepository = container.seoulPublicRepository,
                    reservationRepository = container.reservationRepository,
                    serviceRepository = container.serviceRepository,
                    regionPrefRepository = container.regionPrefRepository
                )
            }
        }
    }
}

//    private fun isReservationAvailableAbsence(row: Row): Boolean {
//        val currentTimeMillis = System.currentTimeMillis()
//        val rcptbgndtMillis = row.rcptbgndt.toLongOrNull() ?: return false
//        val rcptenddtMillis = row.rcptenddt.toLongOrNull() ?: return false
//
//        return currentTimeMillis >= rcptbgndtMillis && currentTimeMillis <= rcptenddtMillis
//    }
//}

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
