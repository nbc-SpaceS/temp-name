package com.example.seoulpublicservice.databases.usecases

import com.example.seoulpublicservice.databases.ReservationDataSource
import com.example.seoulpublicservice.databases.ReservationEntity

class InsertReservationUseCase(private val reservationDataSource: ReservationDataSource) {
    suspend fun invoke(reservationEntity: ReservationEntity) = reservationDataSource.insert(reservationEntity)
}