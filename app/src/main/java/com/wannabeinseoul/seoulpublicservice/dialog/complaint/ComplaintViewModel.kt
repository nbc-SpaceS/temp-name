package com.wannabeinseoul.seoulpublicservice.dialog.complaint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ComplaintRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.UserRepository
import com.wannabeinseoul.seoulpublicservice.dialog.review.ReviewViewModel
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComplaintViewModel(
    private val idPrefRepository: IdPrefRepository,
    private val userRepository: UserRepository,
    private val complaintRepository: ComplaintRepository
) : ViewModel() {

    private val _resultString: MutableLiveData<String> = MutableLiveData()
    val resultString: LiveData<String> get() = _resultString

    fun addComplaint(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = userRepository.getUserId(name)

            _resultString.postValue(complaintRepository.addComplaint(idPrefRepository.load(), id))
        }
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SeoulPublicServiceApplication)
                val container = application.container
                ComplaintViewModel(
                    idPrefRepository = container.idPrefRepository,
                    userRepository = container.userRepository,
                    complaintRepository = container.complaintRepository
                )
            }
        }
    }
}