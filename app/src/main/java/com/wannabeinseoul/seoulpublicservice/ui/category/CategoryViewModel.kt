package com.wannabeinseoul.seoulpublicservice.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.db_by_memory.DbMemoryRepository
import com.wannabeinseoul.seoulpublicservice.pref.CategoryPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.wannabeinseoul.seoulpublicservice.seoul.Row
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepository
import com.wannabeinseoul.seoulpublicservice.ui.recommendation.convertToRecommendationDataList
import com.wannabeinseoul.seoulpublicservice.usecase.GetAll2000UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryPrefRepository: CategoryPrefRepository,
    private val regionPrefRepository: RegionPrefRepository,
    private val getAll2000UseCase: GetAll2000UseCase,
    private val seoulPublicRepository: SeoulPublicRepository,
    private val dbMemoryRepository: DbMemoryRepository,
) : ViewModel() {

    private val _categories = MutableLiveData<List<CategoryData>>(emptyList())
    val categories: LiveData<List<CategoryData>> get() = _categories

    private val _isListEmpty = MutableLiveData<Boolean>()
    val isListEmpty: LiveData<Boolean> get() = _isListEmpty

    fun fetchCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rowList = getAll2000UseCase()
                val categories = rowList.convertToCategoryDataList()
//                _categories.postValue(categories)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateList (areanm: String, minclassnm: String) {
        val filteredList = dbMemoryRepository.getFiltered(areanm = listOf(areanm), minclassnm = listOf(minclassnm)).convertToCategoryDataList()
        _categories.value = filteredList
        _isListEmpty.value = filteredList.isEmpty()
        //minclassnm은 소분류명
    }

    private fun isReservationAvailableAbsence(row: Row): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        val rcptbgndtMillis = row.rcptbgndt.toLongOrNull() ?: return false
        val rcptenddtMillis = row.rcptenddt.toLongOrNull() ?: return false

        return currentTimeMillis >= rcptbgndtMillis && currentTimeMillis <= rcptenddtMillis
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as SeoulPublicServiceApplication).container
                CategoryViewModel(
                    seoulPublicRepository = container.seoulPublicRepository,
                    categoryPrefRepository = container.categoryPrefRepository,
                    regionPrefRepository = container.regionPrefRepository,
                    getAll2000UseCase = container.getAll2000UseCase,
                    dbMemoryRepository = container.dbMemoryRepository,
                )
            }
        }
    }
}