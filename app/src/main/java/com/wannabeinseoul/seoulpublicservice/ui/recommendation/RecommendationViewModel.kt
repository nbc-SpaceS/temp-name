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
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch


class RecommendationViewModel(
    private val reservationRepository: ReservationRepository,
    private val serviceRepository: ServiceRepository,
    private val regionPrefRepository: RegionPrefRepository,
) : ViewModel() {

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
        loadData()
    }

    fun refreshData() {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedRegions = regionPrefRepository.load()
            val items = mutableListOf<Pair<String, String>>()
            val regionItems = mutableListOf<Pair<String, String>>()
            val randomItems = mutableListOf<Pair<String, String>>()

            // 랜덤으로 두 개의 서비스 항목 선택
            val randomSelectedItems = listOf(
                Pair("댄스", "댄스와 관련된 서비스"),
                Pair("체육시설", "체육시설에 관한 서비스"),
                Pair("문화체험", "문화체험에 관한 서비스"),
                Pair("시설대관", "공간시설과 관한 서비스"),
            ).shuffled().take(2)

            // 선택된 서비스 항목을 추가
            items.addAll(randomSelectedItems)

            items.add(Pair("청소년", "청소년을 위한 서비스"))
            items.add(Pair("장애인", "장애인을 위한 서비스"))

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
                reviewCount = counts[it],
                serviceName = reservationEntities[it].SVCNM,
            )
        }
    }

    suspend fun getAdditionalQuery(query: String, num: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val searchText = reservationRepository.searchText(query)

            if (searchText.size >= num) {
                val reservationEntities = searchText.slice(num - 5 until num)
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
            isLoading.postValue(true)

            viewModelScope.launch(Dispatchers.IO) {
                val selectedRegions = regionPrefRepository.load()

                val regionItems = mutableListOf<Pair<String, String>>()

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
                val container = (this[APPLICATION_KEY] as SeoulPublicServiceApplication).container
                RecommendationViewModel(
                    reservationRepository = container.reservationRepository,
                    serviceRepository = container.serviceRepository,
                    regionPrefRepository = container.regionPrefRepository
                )
            }
        }
    }
}