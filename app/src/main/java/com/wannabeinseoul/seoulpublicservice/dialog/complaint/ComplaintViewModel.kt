package com.wannabeinseoul.seoulpublicservice.dialog.complaint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.usecase.ComplaintUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComplaintViewModel(
    private val complaintUserUseCase: ComplaintUserUseCase
) : ViewModel() {

    private val _resultString: MutableLiveData<String> = MutableLiveData()
    val resultString: LiveData<String> get() = _resultString

    fun addComplaint(userInfo: ComplaintUserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            _resultString.postValue(complaintUserUseCase(userInfo))
        }
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SeoulPublicServiceApplication)
                val container = application.container
                ComplaintViewModel(
                    complaintUserUseCase = container.complaintUserUseCase
                )
            }
        }
    }
}