package com.example.seoulpublicservice.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seoulpublicservice.databases.ReservationRepository

class HomeViewModelFactory(private val reservationRepository: ReservationRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(reservationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}