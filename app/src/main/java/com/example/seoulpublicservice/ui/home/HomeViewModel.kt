package com.example.seoulpublicservice.ui.home

import androidx.lifecycle.*
import com.example.seoulpublicservice.databases.ReservationEntity
import com.example.seoulpublicservice.databases.ReservationRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val reservationRepository: ReservationRepository
) : ViewModel() {
    val getAll: LiveData<List<ReservationEntity>> = reservationRepository.allItems.asLiveData()

    fun insert(reservation: ReservationEntity) = viewModelScope.launch {
        reservationRepository.insertItem(reservation)
    }


    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}