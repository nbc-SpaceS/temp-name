package com.example.seoulpublicservice.databases

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ReservationRepository(private val reservationDAO: ReservationDAO) {
    val allItems: Flow<List<ReservationEntity>> = reservationDAO.getAll()

    @WorkerThread
    suspend fun insertItem(reservation: ReservationEntity) {
        reservationDAO.insert(reservation)
    }
}