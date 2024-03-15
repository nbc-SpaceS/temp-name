package com.wannabeinseoul.seoulpublicservice.ui.home

import androidx.lifecycle.ViewModel
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.SearchPrefRepository

class HomeViewModel(
    private val regionPrefRepository: RegionPrefRepository,
    private val searchPrefRepository: SearchPrefRepository,
    private val reservationRepository: ReservationRepository
) : ViewModel() {

}