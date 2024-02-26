package com.example.seoulpublicservice.databases

interface ReservationRepository {       // get
    /**
     * @property insertAll DB에 API로 받아온 데이터 리스트로 전부 삽입
     * @param reservationList ReservationEntity 타입의 List를 받아오기
     */
    suspend fun insertAll(reservationList: List<ReservationEntity>)

    /**
     * @property deleteAll 테이블만 남기고 DB의 모든 데이터를 삭제(테스트용)
     */
    suspend fun deleteAll()

    /**
     * @property getAllReservations 모든 아이템을 List로 출력
     * @return `List<ReservationEntity>` 타입으로 반환
     */
    suspend fun getAllReservations(): List<ReservationEntity>

    /**
     * @property getReservationsWithBigType 입력된 대분류명으로 출력
     * @param type 대분류명
     * @return `List<ReservationEntity>` 타입으로 반환
     */
    suspend fun getReservationsWithBigType(type: String): List<ReservationEntity>

    /**
     * @property getReservationsWithSmallType 입력된 소분류명으로 출력
     * @param type 소분류명
     * @return `List<ReservationEntity>` 타입으로 반환
     */
    suspend fun getReservationsWithSmallType(type: String): List<ReservationEntity>
}

class ReservationRepositoryImpl(
    private val reservationDAO: ReservationDAO
): ReservationRepository {
    override suspend fun insertAll(reservationList: List<ReservationEntity>) {
        reservationDAO.insertAll(reservationList)
    }
    override suspend fun deleteAll() {
        reservationDAO.deleteAll()
    }
    override suspend fun getAllReservations() = reservationDAO.getAll()
    override suspend fun getReservationsWithBigType(type: String) = reservationDAO.getItemsWithBigType(type)
    override suspend fun getReservationsWithSmallType(type: String) = reservationDAO.getItemsWithSmallType(type)
}