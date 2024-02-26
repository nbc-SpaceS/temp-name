package com.example.seoulpublicservice.databases

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

interface ReservationRepository {
    val allItems: Flow<List<ReservationEntity>>
    suspend fun insert(reservation: ReservationEntity)
}