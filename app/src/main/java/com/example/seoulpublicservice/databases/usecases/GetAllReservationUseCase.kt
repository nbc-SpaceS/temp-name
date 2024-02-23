package com.example.seoulpublicservice.databases.usecases

import com.example.seoulpublicservice.databases.ReservationDataSource
import com.example.seoulpublicservice.databases.ReservationEntity

class GetAllReservationUseCase(private val reservationDataSource: ReservationDataSource) {
    suspend fun invoke() = reservationDataSource.getAll()
}