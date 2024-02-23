package com.example.seoulpublicservice.databases

class ReservationDataSourceImpl(private val reservationDAO: ReservationDAO) : ReservationDataSource {
    override suspend fun insert(reservationEntity: ReservationEntity) = reservationDAO.insert(reservationEntity)
    override suspend fun delete(reservationEntity: ReservationEntity) = reservationDAO.delete(reservationEntity)
    override suspend fun getAll() = reservationDAO.getAll()
    override suspend fun getItemsWithType(type: String) = reservationDAO.getItemsWithType(type)
    override suspend fun getItemsWithTypeExtended(type: String) = reservationDAO.getItemsWithTypeExtended(type)
}