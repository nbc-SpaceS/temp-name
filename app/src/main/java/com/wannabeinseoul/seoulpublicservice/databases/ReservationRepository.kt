package com.wannabeinseoul.seoulpublicservice.databases

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery

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
     * @property getAll 모든 아이템을 List로 출력
     * @return 빈 칸이 없는 `List<ReservationEntity>` 타입으로 반환
     */
    suspend fun getAll(): List<ReservationEntity>

    /**
     * @property getAll 모든 아이템을 List로 출력
     * @return `List<ReservationEntity>` 타입으로 반환
     */
    suspend fun getAllBefore() : List<ReservationEntity>

    /**
     * @property getAll 모든 아이템을 List로 출력
     * @return 빈 칸을 제외한 `List<ReservationEntity>` 타입으로 반환
     * 왠지 DB에서 제대로 출력이 안되는 느낌이 듬
     */
    suspend fun getAllNOTBlanks() : List<ReservationEntity>

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
     * @property getFilter 필터를 리스트로 받아 아이템 출력하기
     * @param typeSubCategory 소분류명
     * @param typeLocation 관심 지역명
     * @param typeServiceState 접수 가능 여부
     * @param typePay 요금
     * @return `List<ReservationEntity>`
     */
    suspend fun getFilter(typeSubCategory: List<String>, typeLocation: List<String>, typeServiceState: List<String>, typePay: List<String>) : List<ReservationEntity>

    /**
     * @property getService 서비스 ID를 이용해 서비스를 가져오기
     * @param serviceID 서비스 ID
     * @return `ReservationEntity`
     */
    fun getService(serviceID: String) : ReservationEntity

    suspend fun searchText(text: String) : List<ReservationEntity>

    /**
     * @property searchFilter 검색어, 필터를 이용해 해당하는 서비스를 가져오기
     * @param text 검색어
     * @param typeSub 소분류명
     * @param typeLoc 관심 지역명
     * @param typeSvc 접수 가능 여부
     * @param typePay 요금
     * @return `List<ReservationEntity>`
     */
    suspend fun searchFilter(
        text: String,
        typeSub: List<String>,
        typeLoc: List<String>,
        typeSvc: List<String>,
        typePay: List<String>
    ) : List<ReservationEntity>
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
    override suspend fun getAll() : List<ReservationEntity> {
        val redesignedList: List<ReservationEntity> = reservationDAO.getNOTEmptyXY()
        deleteAll()
        insertAll(redesignedList)
        return reservationDAO.getAll()
    }
    override suspend fun getAllBefore() = reservationDAO.getAll()
    override suspend fun getAllNOTBlanks(): List<ReservationEntity> = reservationDAO.getNOTEmptyXY()
    override suspend fun getReservationsWithBigType(type: String) = reservationDAO.getItemsWithBigType(type)
    override suspend fun getReservationsWithSmallType(type: String) = reservationDAO.getItemsWithSmallType(type)
    override suspend fun getReservationsWithSmallTypes(types: List<String>) = reservationDAO.getItemsWithSmallTypes(types)

    override suspend fun getFilter(
        typeSubCategory: List<String>,
        typeLocation: List<String>,
        typeServiceState: List<String>,
        typePay: List<String>
    ): List<ReservationEntity> {
        var subStr = ""
        var locStr = ""
        var svcStr = ""
        var payStr = ""
        when {
            typeSubCategory.size >= 2 -> subStr = typeSubCategory.joinToString("','")
            typeSubCategory.size == 1 && typeSubCategory.first().isNotEmpty() -> subStr = typeSubCategory.joinToString("")
            typeSubCategory.isEmpty() || typeSubCategory.size == 1 && typeSubCategory.first().isEmpty() -> subStr = reservationDAO.getSubList().joinToString("','")
        }
        when {
            typeLocation.size >= 2 -> locStr = typeLocation.joinToString("','")
            typeLocation.size == 1 && typeLocation.first().isNotEmpty() -> locStr = typeLocation.joinToString("")
            typeLocation.isEmpty() || typeLocation.size == 1 && typeLocation.first().isEmpty() -> locStr = reservationDAO.getLocList().joinToString("','")
        }
        when {
            typeServiceState.size >= 2 -> svcStr = typeServiceState.joinToString("','")
            typeServiceState.size == 1 && typeServiceState.first().isNotEmpty() -> svcStr = typeServiceState.joinToString("")
            typeServiceState.isEmpty() || typeServiceState.size == 1 && typeServiceState.first().isEmpty() -> svcStr = reservationDAO.getSvcList().joinToString("','")
        }
        when {
            typePay.size >= 2 -> payStr = typePay.joinToString("','")
            typePay.size == 1 && typePay.first().isNotEmpty() -> payStr = typePay.joinToString("")
            typePay.isEmpty() || typePay.size == 1 && typePay.first().isEmpty() -> payStr = reservationDAO.getPayList().joinToString("','")
        }
        val queryStr = "SELECT * FROM ReservationEntity WHERE (MINCLASSNM IN ('$subStr')) AND (AREANM IN ('$locStr')) AND (SVCSTATNM IN ('$svcStr')) AND (PAYATNM IN ('$payStr'))"
        Log.i("This is ReservationRepository","Query문 : $queryStr\n")
        val query = SimpleSQLiteQuery(queryStr)
        return reservationDAO.rawQuery(query)
    }

    override fun getService(serviceID: String) = reservationDAO.getService(serviceID)

    override suspend fun searchText(text: String): List<ReservationEntity> {
//        val queryStr = "SELECT * FROM ReservationEntity WHERE (SVCNM LIKE '%${text.trim()}%' OR PLACENM LIKE '%${text.trim()}%' OR AREANM LIKE '%${text.trim()}%' OR TELNO LIKE '%${text.trim()}%' OR MINCLASSNM LIKE '%${text.trim()}%' OR USETGTINFO LIKE '%${text.trim()}%')"
        val queryStr = "SELECT * FROM ReservationEntity WHERE SVCID IN (SELECT SVCID FROM ReservationEntity WHERE (SVCNM LIKE '%${text.trim()}%' OR PLACENM LIKE '%${text.trim()}%' OR AREANM LIKE '%${text.trim()}%' OR TELNO LIKE '%${text.trim()}%' OR MINCLASSNM LIKE '%${text.trim()}%' OR USETGTINFO LIKE '%${text.trim()}%'))"
        val query = SimpleSQLiteQuery(queryStr)
        return reservationDAO.rawQuery(query)
    }

    override suspend fun searchFilter(
        text: String,
        typeSub: List<String>,
        typeLoc: List<String>,
        typeSvc: List<String>,
        typePay: List<String>
    ): List<ReservationEntity> {
        var subStr = ""
        var locStr = ""
        var svcStr = ""
        var payStr = ""
        when {
            typeSub.size >= 2 -> subStr = typeSub.joinToString("','")
            typeSub.size == 1 && typeSub.first().isNotEmpty() -> subStr = typeSub.joinToString("")
            typeSub.isEmpty() || typeSub.size == 1 && typeSub.first().isEmpty() -> subStr = reservationDAO.getSubList().joinToString("','")
        }
        when {
            typeLoc.size >= 2 -> locStr = typeLoc.joinToString("','")
            typeLoc.size == 1 && typeLoc.first().isNotEmpty() -> locStr = typeLoc.joinToString("")
            typeLoc.isEmpty() || typeLoc.size == 1 && typeLoc.first().isEmpty() -> locStr = reservationDAO.getLocList().joinToString("','")
        }
        when {
            typeSvc.size >= 2 -> svcStr = typeSvc.joinToString("','")
            typeSvc.size == 1 && typeSvc.first().isNotEmpty() -> svcStr = typeSvc.joinToString("")
            typeSvc.isEmpty() || typeSvc.size == 1 && typeSvc.first().isEmpty() -> svcStr = reservationDAO.getSvcList().joinToString("','")
        }
        when {
            typePay.size >= 2 -> payStr = typePay.joinToString("','")
            typePay.size == 1 && typePay.first().isNotEmpty() -> payStr = typePay.joinToString("")
            typePay.isEmpty() || typePay.size == 1 && typePay.first().isEmpty() -> payStr = reservationDAO.getPayList().joinToString("','")
        }
        val queryStr = "SELECT * FROM ReservationEntity " +
                "WHERE (MINCLASSNM IN ('$subStr')) AND (AREANM IN ('$locStr')) AND (SVCSTATNM IN ('$svcStr')) AND (PAYATNM IN ('$payStr')) " +
                "AND (SVCNM LIKE '%${text.trim()}%' OR PLACENM LIKE '%${text.trim()}%' OR AREANM LIKE '%${text.trim()}%' OR TELNO LIKE '%${text.trim()}%' OR MINCLASSNM LIKE '%${text.trim()}%' OR USETGTINFO LIKE '%$text%')"
        Log.i("This is ReservationRepository","Query문 : $queryStr\n")
        val query = SimpleSQLiteQuery(queryStr)
        return reservationDAO.rawQuery(query)
    }
}
