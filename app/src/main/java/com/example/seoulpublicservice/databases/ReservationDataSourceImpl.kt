package com.example.seoulpublicservice.databases

class ReservationDataSourceImpl(private val reservationDAO: ReservationDAO) : ReservationDataSource {
    override fun insert(reservationEntity: ReservationEntity) = reservationDAO.insert(reservationEntity)
    override fun delete(reservationEntity: ReservationEntity) = reservationDAO.delete(reservationEntity)
    override fun getAll() = reservationDAO.getAll()
    override fun getItemsWithType(type: String) = reservationDAO.getItemsWithType(type)
    override fun getItemsWithTypeExtended(type: String) = reservationDAO.getItemsWithTypeExtended(type)
}