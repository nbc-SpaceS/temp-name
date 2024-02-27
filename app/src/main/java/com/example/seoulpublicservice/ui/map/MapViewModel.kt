package com.example.seoulpublicservice.ui.map

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
import com.example.seoulpublicservice.pref.FilterPrefRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel(
    private val filterPrefRepository: FilterPrefRepository,
    private val reservationRepository: ReservationRepository
) : ViewModel() {

    private val _loadedFilterOptions: MutableLiveData<List<List<String>>> = MutableLiveData()
    val loadedFilterOptions: LiveData<List<List<String>>> get() = _loadedFilterOptions

    private val _hasFilter: MutableLiveData<Boolean> = MutableLiveData()
    val hasFilter: LiveData<Boolean> get() = _hasFilter

    private val _filteringData: MutableLiveData<List<ReservationEntity>> = MutableLiveData()
    val filteringData: LiveData<List<ReservationEntity>> get() = _filteringData

    fun load() {
        val loadedData = filterPrefRepository.load()

        _loadedFilterOptions.value = loadedData

        _hasFilter.value = loadedData.any { it.isNotEmpty() }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _filteringData.postValue(reservationRepository.getReservationsWithSmallTypes(loadedData[0]))
            }
        }
    }

    companion object {
        /** 뷰모델팩토리에서 의존성주입을 해준다 */
        val factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SeoulPublicServiceApplication)
                val container = application.container
                MapViewModel(
                    filterPrefRepository = container.filterPrefRepository,
                    reservationRepository = container.reservationRepository
                )
            }
        }
    }
}