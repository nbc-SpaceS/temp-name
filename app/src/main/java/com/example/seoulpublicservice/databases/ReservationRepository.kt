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

    /**
     * @property getReservationsWithSmallTypes 입력된 소분류명들로 출력
     * @param types 소분류명들
     * @return `List<ReservationEntity>` 타입으로 반환
     */
    suspend fun getReservationsWithSmallTypes(types: List<String>): List<ReservationEntity>

    /**
     * @property getFilterItemsOR 필터를 리스트로 받아 아이템 출력하기
     * @param typeSmall 소분류명
     * @param typeLocation 관심 지역명
     * @param typeServiceState 접수 가능 여부
     * @param typePay 요금
     * @return `List<ReservationEntity>`
     */
    suspend fun getFilterItemsOR(typeSmall: List<String>, typeLocation: List<String>, typeServiceState: List<String>, typePay: List<String>) : List<ReservationEntity>

    suspend fun getQueries(typeMin: List<String>, typeArea: List<String>, typeSvc: List<String>, typePay: List<String>) : List<ReservationEntity>
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

    override suspend fun getReservationsWithSmallTypes(types: List<String>) = reservationDAO.getItemsWithSmallTypes(types)

    override suspend fun getFilterItemsOR(
        typeSubCategory: List<String>,
        typeLocation: List<String>,
        typeServiceState: List<String>,
        typePay: List<String>
    ): List<ReservationEntity> {
        val returnList: MutableList<ReservationEntity> = mutableListOf()
        returnList += when {
            typeSubCategory.isNotEmpty() -> reservationDAO.getItemsWithSmallTypes(typeSubCategory)
            typeLocation.isNotEmpty() -> reservationDAO.getLocation(typeLocation)
            typeServiceState.isNotEmpty() -> reservationDAO.getServiceState(typeServiceState)
            typePay.isNotEmpty() -> reservationDAO.getPay(typePay)
            else -> return returnList.toList()
        }
        return returnList.toList()
    }

    override suspend fun getQueries(
        typeMin: List<String>,
        typeArea: List<String>,
        typeSvc: List<String>,
        typePay: List<String>
    ) = reservationDAO.getQueries(typeMin, typeArea, typeSvc, typePay)
}