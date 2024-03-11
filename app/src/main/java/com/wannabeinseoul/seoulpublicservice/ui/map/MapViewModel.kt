package com.wannabeinseoul.seoulpublicservice.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.seoul.Row
import com.wannabeinseoul.seoulpublicservice.usecase.FilterServiceDataOnMapUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.GetSavedServiceUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.LoadSavedFilterOptionsUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.MappingDetailInfoWindowUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.SaveServiceUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(
    private val loadSavedFilterOptionsUseCase: LoadSavedFilterOptionsUseCase,
    private val filterServiceDataOnMapUseCase: FilterServiceDataOnMapUseCase,
    private val saveServiceUseCase: SaveServiceUseCase,
    private val mappingDetailInfoWindowUseCase: MappingDetailInfoWindowUseCase,
    private val getSavedServiceUseCase: GetSavedServiceUseCase
) : ViewModel() {

    private var readyMap: Boolean = false
    private var readyData: Boolean = false

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

    fun setServiceData() {
        _canStart.value = false
        readyData = false
        val savedOptions = loadSavedOptions()

        _filterCount = savedOptions.count { it.isNotEmpty() }
        _hasFilter.value = savedOptions.any { it.isNotEmpty() }

        viewModelScope.launch(Dispatchers.IO) {
            _filteringData.postValue(filterServiceDataOnMapUseCase(savedOptions))

            readyData = true
            if (readyMap) {
                _canStart.postValue(true)
            }
        }
    }

    fun loadSavedOptions(): List<List<String>> = loadSavedFilterOptionsUseCase()

    fun checkReadyMap() {
        readyMap = true
        if (readyData) {
            _canStart.postValue(true)
        }
    }

    fun saveService(id: String) {
        saveServiceUseCase(id)
    }

    fun moveReservationPage(url: String) {
        _moveToUrl.value = url
    }

    fun changeVisible(flag: Boolean) {
        _visibleInfoWindow.value = flag
    }

    fun updateInfo(info: List<Row>) {
        _updateData.value = mappingDetailInfoWindowUseCase(info)
    }

    fun initMap() {
        readyMap = false
    }

    fun getSavedPrefRepository() = getSavedServiceUseCase()

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
                    loadSavedFilterOptionsUseCase = container.loadSavedFilterOptionsUseCase,
                    filterServiceDataOnMapUseCase = container.filterServiceDataOnMapUseCase,
                    saveServiceUseCase = container.saveServiceUseCase,
                    mappingDetailInfoWindowUseCase = container.mappingDetailInfoWindowUseCase,
                    getSavedServiceUseCase = container.getSavedServiceUseCase
                )
            }
        }
    }
}