package com.wannabeinseoul.seoulpublicservice.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.db_by_memory.DbMemoryRepository
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.SavedPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.SearchPrefRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeViewModel(
    private val regionPrefRepository: RegionPrefRepository,
    private val searchPrefRepository: SearchPrefRepository,
    private val reservationRepository: ReservationRepository,
    private val dbMemoryRepository: DbMemoryRepository,
    private val savedPrefRepository: SavedPrefRepository
) : ViewModel() {

    private var selectedRegions: List<String> = emptyList()

    private var _randomService: List<String> = emptyList()
    val randomService: List<String> get() = _randomService

    private var _notificationSign: MutableLiveData<Boolean> = MutableLiveData()
    val notificationSign: MutableLiveData<Boolean> get() = _notificationSign

    private val _updateSelectedRegions: MutableLiveData<List<String>> = MutableLiveData()
    val updateSelectedRegions: LiveData<List<String>> get() = _updateSelectedRegions

    private var _displaySearchResult: MutableLiveData<List<ReservationEntity>> = MutableLiveData()
    val displaySearchResult: LiveData<List<ReservationEntity>> get() = _displaySearchResult

    private var _displaySearchHistory: MutableLiveData<Pair<List<String>, SearchPrefRepository>> =
        MutableLiveData()
    val displaySearchHistory: LiveData<Pair<List<String>, SearchPrefRepository>> get() = _displaySearchHistory

    private val _updateViewPagerCategory: MutableLiveData<List<Pair<String, Int>>> =
        MutableLiveData()
    val updateViewPagerCategory: LiveData<List<Pair<String, Int>>> get() = _updateViewPagerCategory

    fun clearSearchResult() {
        if (_displaySearchResult.value?.isNotEmpty() == true) _displaySearchResult.value =
            emptyList()
        if (_displaySearchHistory.value?.first?.isNotEmpty() == true) _displaySearchHistory.value =
            Pair(
                emptyList(), searchPrefRepository
            )
    }

    fun setViewPagerCategory(area: String) {
        _updateViewPagerCategory.value = dbMemoryRepository.getFilteredCountWithMaxClass(
            listOf(
                "체육시설",
                "교육강좌",
                "문화체험",
                "공간시설",
                "진료복지"
            ), area
        ).filter { it.second != 0 }.map {
            if (it.first == "공간시설") Pair("시설대관", it.second)
            else it
        }
    }

    fun setRandomService() {
        _randomService = dbMemoryRepository.getFilteredByDate()
    }

    fun setupRegions() {
        selectedRegions = regionPrefRepository.load().toMutableList()

        when {
            selectedRegions.isNotEmpty() -> {
                regionPrefRepository.saveSelectedRegion(1)
                _updateSelectedRegions.value = selectedRegions
            }

            else -> {
                _updateSelectedRegions.value = emptyList()
            }
        }
    }

    fun performSearch(query: String) {
        // 검색어가 비어있지 않을 때만 검색어가 저장됨
        if (query.isNotEmpty()) {
            saveSearchQuery(query)
            Log.d("Search", "Saved search query: $query")
        }

        viewModelScope.launch(Dispatchers.IO) {
            displaySearchResults(query)
        }
    }

    private fun saveSearchQuery(query: String) {
        searchPrefRepository.save(query)
        Log.d("Search", "Saved search query: $query") // 로그 찍기
    }

    private suspend fun displaySearchResults(query: String) {
        // searchText 메소드를 호출하여 검색 결과를 가져옴
        _displaySearchResult.postValue(reservationRepository.searchText(query))
    }

    fun showSearchHistory() {
        // 포커스가 EditText에 있을 때 저장된 검색어를 불러옴
        _displaySearchHistory.value =
            Pair(searchPrefRepository.load().toMutableList(), searchPrefRepository)
    }

    fun saveSelectedRegion(index: Int) {
        regionPrefRepository.saveSelectedRegion(index)
    }

    fun updateNotificationSign() {
        viewModelScope.launch(Dispatchers.IO) {
            val datePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")

            val savedServiceList = savedPrefRepository.getSvcidList().map {
                reservationRepository.getService(it)
            }

            // 예약 시작까지 하루 남은 서비스의 개수
            val list = savedServiceList.filter {
                datePattern.format(LocalDateTime.parse(it.RCPTBGNDT, formatter)) > datePattern.format(
                    LocalDateTime.now()) && datePattern.format(LocalDateTime.parse(it.RCPTBGNDT, formatter)) < datePattern.format(
                    LocalDateTime.now().plusDays(2))
            }.size

            // 예약 마감까지 하루 남은 서비스의 개수
            val list2 = savedServiceList.filter {
                datePattern.format(LocalDateTime.parse(it.RCPTENDDT, formatter)) < datePattern.format(
                    LocalDateTime.now()) && datePattern.format(LocalDateTime.parse(it.RCPTENDDT, formatter)) > datePattern.format(
                    LocalDateTime.now().minusDays(2))
            }.size

            // 예약 가능한 서비스의 개수
            val list3 = savedServiceList.filter {
                datePattern.format(LocalDateTime.parse(it.RCPTBGNDT, formatter)) == datePattern.format(
                    LocalDateTime.now())
            }.size

            _notificationSign.postValue(list != 0 || list2 != 0 || list3 != 0)
        }
    }

    fun hideNotificationSign() {
        _notificationSign.value = false
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SeoulPublicServiceApplication)
                val container = application.container
                HomeViewModel(
                    regionPrefRepository = container.regionPrefRepository,
                    searchPrefRepository = container.searchPrefRepository,
                    reservationRepository = container.reservationRepository,
                    dbMemoryRepository = container.dbMemoryRepository,
                    savedPrefRepository = container.savedPrefRepository
                )
            }
        }
    }
}