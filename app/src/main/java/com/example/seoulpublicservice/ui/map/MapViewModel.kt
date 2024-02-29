package com.example.seoulpublicservice.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.seoulpublicservice.SeoulPublicServiceApplication
import com.example.seoulpublicservice.databases.ReservationEntity
import com.example.seoulpublicservice.databases.ReservationRepository
import com.example.seoulpublicservice.db_by_memory.DbMemoryRepository
import com.example.seoulpublicservice.pref.FilterPrefRepository
import com.example.seoulpublicservice.seoul.Row
import com.example.seoulpublicservice.util.RoomRowMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel(
    private val filterPrefRepository: FilterPrefRepository,
    private val reservationRepository: ReservationRepository,
    private val dbMemoryRepository: DbMemoryRepository,
) : ViewModel() {

    private var readyMap: Boolean = false
    private var readyData: Boolean = false

    private val hash: HashMap<Pair<String, String>, List<Row>> = hashMapOf()

    private var _hasFilter: MutableLiveData<Boolean> = MutableLiveData()
    val hasFilter: LiveData<Boolean> get() = _hasFilter

    private var _canStart: MutableLiveData<Boolean> = MutableLiveData()
    val canStart: LiveData<Boolean> get() = _canStart

    private var _visibleInfoWindow: MutableLiveData<Boolean> = MutableLiveData()
    val visibleInfoWindow: LiveData<Boolean> get() = _visibleInfoWindow

    private var _filteringData: MutableLiveData<HashMap<Pair<String, String>, List<Row>>> =
        MutableLiveData()
    val filteringData: LiveData<HashMap<Pair<String, String>, List<Row>>> get() = _filteringData

    private var _updateData: MutableLiveData<List<Row>> = MutableLiveData()
    val updateData: LiveData<List<Row>> get() = _updateData

    private var _moveToUrl: MutableLiveData<String> = MutableLiveData()
    val moveToUrl: LiveData<String> get() = _moveToUrl

    private var _shareUrl: MutableLiveData<String> = MutableLiveData()
    val shareUrl: LiveData<String> get() = _shareUrl

    private var _detailInfoId: MutableLiveData<String> = MutableLiveData()
    val detailInfoId: LiveData<String> get() = _detailInfoId

    fun loadSavedOptions() {
        _canStart.value = false
        readyData = false
        val loadedData = filterPrefRepository.load()

        _hasFilter.value = loadedData.any { it.isNotEmpty() }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val item = dbMemoryRepository.getFiltered(
                    loadedData.subList(0, 5).flatten(),
                    loadedData[5],
                    loadedData[6],
                    loadedData[7]
                )
                hash.clear()
                for (i in item) {
                    if (hash.containsKey(Pair(i.y, i.x))) {
                        hash[Pair(i.y, i.x)] = hash[Pair(i.y, i.x)].orEmpty().toMutableList() + i
                    } else {
                        hash[Pair(i.y, i.x)] = listOf(i)
                    }
                }

                _filteringData.postValue(
                    hash
                )

                readyData = true
                checkCanDraw()
            }
        }
    }

    fun checkReadyMap() {
        readyMap = true
        checkCanDraw()
    }

    private fun checkCanDraw() {
        if (readyMap && readyData) {
            _canStart.postValue(true)
        }
    }

    fun moveReservationPage(url: String) {
        _moveToUrl.value = url
    }

    fun shareReservationPage(url: String) {
        _shareUrl.value = url
    }

    fun moveDetailPage(id: String) {
        _detailInfoId.value = id
    }

    fun changeVisible(flag: Boolean) {
        _visibleInfoWindow.value = flag
    }

    fun updateInfo(info: List<Row>) {
        _updateData.value = info
    }

    fun initMap() {
        readyMap = false
    }

    fun clearData() {
        _filteringData = MutableLiveData()
        _hasFilter = MutableLiveData()
        _visibleInfoWindow = MutableLiveData()
        _updateData = MutableLiveData()
        _moveToUrl = MutableLiveData()
        _shareUrl = MutableLiveData()
        _detailInfoId = MutableLiveData()
    }

    companion object {
        /** 뷰모델팩토리에서 의존성주입을 해준다 */
        val factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SeoulPublicServiceApplication)
                val container = application.container
                MapViewModel(
                    filterPrefRepository = container.filterPrefRepository,
                    reservationRepository = container.reservationRepository,
                    dbMemoryRepository = container.dbMemoryRepository
                )
            }
        }
    }
}