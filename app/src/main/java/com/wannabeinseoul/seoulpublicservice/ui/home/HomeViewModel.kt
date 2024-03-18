package com.wannabeinseoul.seoulpublicservice.ui.home

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databases.RecentEntity
import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.db_by_memory.DbMemoryRepository
import com.wannabeinseoul.seoulpublicservice.pref.RecentPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.SearchPrefRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val regionPrefRepository: RegionPrefRepository,
    private val searchPrefRepository: SearchPrefRepository,
    private val reservationRepository: ReservationRepository,
    private val dbMemoryRepository: DbMemoryRepository,
    private val recentPrefRepository: RecentPrefRepository
) : ViewModel() {

    private var selectedRegions: List<String> = emptyList()

    private var _randomService: List<String> = emptyList()
    val randomService: List<String> get() = _randomService

    private val _updateSelectedRegions: MutableLiveData<List<String>> = MutableLiveData()
    val updateSelectedRegions: LiveData<List<String>> get() = _updateSelectedRegions

    private val _displaySearchResult: MutableLiveData<List<ReservationEntity>> = MutableLiveData()
    val displaySearchResult: LiveData<List<ReservationEntity>> get() = _displaySearchResult

    private val _displaySearchHistory: MutableLiveData<Pair<List<String>, SearchPrefRepository>> =
        MutableLiveData()
    val displaySearchHistory: LiveData<Pair<List<String>, SearchPrefRepository>> get() = _displaySearchHistory

    private val _recentData: MutableLiveData<List<RecentEntity>> = MutableLiveData()
    val recentData: LiveData<List<RecentEntity>> get() = _recentData

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

    fun loadRecentData() {
        _recentData.value = recentPrefRepository.getRecent()
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
                    recentPrefRepository = container.recentPrefRepository
                )
            }
        }
    }
}