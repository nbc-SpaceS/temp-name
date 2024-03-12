package com.wannabeinseoul.seoulpublicservice.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ServiceRepository
import com.wannabeinseoul.seoulpublicservice.db_by_memory.DbMemoryRepository
import com.wannabeinseoul.seoulpublicservice.pref.CategoryPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.wannabeinseoul.seoulpublicservice.seoul.Row
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepository
import com.wannabeinseoul.seoulpublicservice.ui.recommendation.RecommendationData
import com.wannabeinseoul.seoulpublicservice.ui.recommendation.convertToRecommendationDataList
import com.wannabeinseoul.seoulpublicservice.usecase.GetAll2000UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryPrefRepository: CategoryPrefRepository,
    private val regionPrefRepository: RegionPrefRepository,
    private val getAll2000UseCase: GetAll2000UseCase,
    private val seoulPublicRepository: SeoulPublicRepository,
    private val reservationRepository: ReservationRepository,
    private val serviceRepository: ServiceRepository,
    private val dbMemoryRepository: DbMemoryRepository,

    ) : ViewModel() {

    private val _categories = MutableLiveData<List<CategoryData>>(emptyList())
    val categories: LiveData<List<CategoryData>> get() = _categories


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
    fun updateList (minclassnm:String) {
        _categories.value = dbMemoryRepository.getFiltered(minclassnm = listOf(minclassnm)).convertToCategoryDataList()
        //minclassnm은 소분류명
    }

    fun getList(query: String, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = reservationRepository.searchText(query)
//            val entity = reservationRepository.getFilter(emptyList(), listOf(query), emptyList(), emptyList())
            val count = serviceRepository.getServiceReviewsCount(entity.take(5).map { it.SVCID })

            val itemList = mutableListOf<CategoryData>()
        }
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
                    reservationRepository = container.reservationRepository,
                    serviceRepository = container.serviceRepository
                )
            }
        }
    }
}