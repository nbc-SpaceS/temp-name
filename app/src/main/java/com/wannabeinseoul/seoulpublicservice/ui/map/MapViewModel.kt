package com.wannabeinseoul.seoulpublicservice.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ReviewEntity
import com.wannabeinseoul.seoulpublicservice.databases.firebase.UserEntity
import com.wannabeinseoul.seoulpublicservice.db_by_memory.DbMemoryRepository
import com.wannabeinseoul.seoulpublicservice.pref.FilterPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.SavedPrefRepository
import com.wannabeinseoul.seoulpublicservice.seoul.Row
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(
    private val filterPrefRepository: FilterPrefRepository,
    private val reservationRepository: ReservationRepository,
    private val savedPrefRepository: SavedPrefRepository,
    private val dbMemoryRepository: DbMemoryRepository
) : ViewModel() {

    private var readyMap: Boolean = false
    private var readyData: Boolean = false

    private val hash: HashMap<Pair<String, String>, List<Row>> = hashMapOf()

    private var _filterCount: Int = 0
    val filterCount: Int get() = _filterCount

    private var _hasFilter: MutableLiveData<Boolean> = MutableLiveData()
    val hasFilter: LiveData<Boolean> get() = _hasFilter

    private var _canStart: MutableLiveData<Boolean> = MutableLiveData()
    val canStart: LiveData<Boolean> get() = _canStart

    private var _visibleInfoWindow: MutableLiveData<Boolean> = MutableLiveData()
    val visibleInfoWindow: LiveData<Boolean> get() = _visibleInfoWindow

    private var _filteringData: MutableLiveData<HashMap<Pair<String, String>, List<Row>>> =
        MutableLiveData()
    val filteringData: LiveData<HashMap<Pair<String, String>, List<Row>>> get() = _filteringData

    private var _updateData: MutableLiveData<List<DetailInfoWindow>> = MutableLiveData()
    val updateData: LiveData<List<DetailInfoWindow>> get() = _updateData

    private var _moveToUrl: MutableLiveData<String> = MutableLiveData()
    val moveToUrl: LiveData<String> get() = _moveToUrl

    fun loadSavedOptions() {
        _canStart.value = false
        readyData = false
        val loadedData = filterPrefRepository.load()

        _filterCount = loadedData.count { it.isNotEmpty() }
        _hasFilter.value = loadedData.any { it.isNotEmpty() }

        viewModelScope.launch(Dispatchers.IO) {

//                var item = RoomRowMapper.mappingRoomToRow(
//                    reservationRepository.getFilter(
//                        loadedData.subList(0, 5).flatten(),
//                        loadedData.subList(5, 7).flatten(),
//                        loadedData[7],
//                        loadedData[8],
//                    )
//                )

            val item = dbMemoryRepository.getFiltered(
                loadedData.subList(0, 5).flatten(),
                loadedData.subList(5, 7).flatten(),
                loadedData[7],
                loadedData[8],
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

    fun checkReadyMap() {
        readyMap = true
        checkCanDraw()
    }

    private fun checkCanDraw() {
        if (readyMap && readyData) {
            _canStart.postValue(true)
        }
    }

    fun saveService(id: String) {
        if (savedPrefRepository.contains(id)) {
            savedPrefRepository.remove(id)
        } else {
            savedPrefRepository.addSvcid(id)
        }
    }

    fun moveReservationPage(url: String) {
        _moveToUrl.value = url
    }

    fun changeVisible(flag: Boolean) {
        _visibleInfoWindow.value = flag
    }

    fun updateInfo(info: List<Row>) {
        _updateData.value = info.map {
            DetailInfoWindow(
                svcid = it.svcid,
                imgurl = it.imgurl,
                areanm = it.areanm,
                svcnm = it.svcnm,
                payatnm = it.payatnm,
                svcstatnm = it.svcstatnm,
                svcurl = it.svcurl,
                saved = savedPrefRepository.contains(it.svcid)
            )
        }
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
                    savedPrefRepository = container.savedPrefRepository,
                    dbMemoryRepository = container.dbMemoryRepository
                )
            }
        }
    }
}