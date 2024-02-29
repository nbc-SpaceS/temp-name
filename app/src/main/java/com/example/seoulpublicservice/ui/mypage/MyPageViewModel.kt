package com.example.seoulpublicservice.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.seoulpublicservice.SeoulPublicServiceApplication
import com.example.seoulpublicservice.db_by_memory.DbMemoryRepository
import com.example.seoulpublicservice.pref.SavedPrefRepository
import com.example.seoulpublicservice.seoul.Row
import com.example.seoulpublicservice.usecase.GetDetailSeoulUseCase

class MyPageViewModel(
    private val savedPrefRepository: SavedPrefRepository,
    private val getDetailSeoulUseCase: GetDetailSeoulUseCase,
    private val dbMemoryRepository: DbMemoryRepository,
) : ViewModel() {

    private var _savedList: MutableLiveData<List<Row?>> = MutableLiveData(emptyList())
    val savedList: LiveData<List<Row?>> get() = _savedList

    fun loadSavedList() {
        val ids = savedPrefRepository.getSvcidList()
        var detailRows: List<Row?>
        _savedList.value = ids.map { dbMemoryRepository.findBySvcid(it) }
    }

    fun clearSavedList() {
        savedPrefRepository.clear()
        _savedList.value = emptyList()
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as SeoulPublicServiceApplication).container
                MyPageViewModel(
                    savedPrefRepository = container.savedPrefRepository,
                    getDetailSeoulUseCase = container.getDetailSeoulUseCase,
                    dbMemoryRepository = container.dbMemoryRepository,
                )
            }
        }
    }

}
