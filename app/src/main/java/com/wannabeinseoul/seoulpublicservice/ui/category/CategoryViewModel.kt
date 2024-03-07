package com.wannabeinseoul.seoulpublicservice.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.pref.CategoryPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepository
import com.wannabeinseoul.seoulpublicservice.usecase.GetAll2000UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryPrefRepository: CategoryPrefRepository,
    private val regionPrefRepository: RegionPrefRepository,
    private val getAll2000UseCase: GetAll2000UseCase,
    private val seoulPublicRepository: SeoulPublicRepository
) : ViewModel() {

    private val _categories = MutableLiveData<List<CategoryData>>()
    val categories: LiveData<List<CategoryData>> get() = _categories

    fun fetchCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 3개의 저장된 지역구 가져오기
//                val selectedRegions = regionPrefRepository.selectedRegions.take(3)
//                // 가져온 지역구에 해당하는 데이터를 모두 가져오기
//                val rowList = getAll2000UseCase(selectedRegions)
//                val categories = rowList.convertToCategoryDataList()
//                _categories.postValue(categories)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as SeoulPublicServiceApplication).container
                CategoryViewModel(
                    seoulPublicRepository = container.seoulPublicRepository,
                    categoryPrefRepository = container.categoryPrefRepository,
                    regionPrefRepository = container.regionPrefRepository,
                    getAll2000UseCase = container.getAll2000UseCase
                )
            }
        }
    }
}