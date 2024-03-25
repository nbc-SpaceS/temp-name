package com.wannabeinseoul.seoulpublicservice.util

import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import com.wannabeinseoul.seoulpublicservice.seoul.Row

//fun ReservationEntity.toRow() = Row(
//    div = "",
//    service = "",
//    gubun = this.GUBUN,
//    svcid = this.SVCID,
//    maxclassnm = this.MAXCLASSNM,
//    minclassnm = this.MINCLASSNM,
//    svcstatnm = this.SVCSTATNM,
//    svcnm = this.SVCNM,
//    payatnm = this.PAYATNM,
//    placenm = this.PLACENM,
//    usetgtinfo = this.USETGTINFO,
//    svcurl = this.SVCURL,
//    x = this.X,
//    y = this.Y,
//    svcopnbgndt = this.SVCOPNBGNDT,
//    svcopnenddt = this.SVCOPNENDDT,
//    rcptbgndt = this.RCPTBGNDT,
//    rcptenddt = this.RCPTENDDT,
//    areanm = this.AREANM,
//    imgurl = this.IMGURL,
//    dtlcont = this.DTLCONT,
//    telno = this.TELNO,
//    vMax = this.V_MAX,
//    vMin = this.V_MIN,
//    revstdday = this.REVSTDDAY.toLong(),
//    revstddaynm = this.REVSTDDAYNM
//)

fun Row.toReservationEntity() = ReservationEntity(
    AREANM = this.areanm ?: "",
    DTLCONT = this.dtlcont ?: "",
    GUBUN = this.gubun ?: "",
    IMGURL = this.imgurl ?: "",
    MAXCLASSNM = this.maxclassnm ?: "",
    MINCLASSNM = this.minclassnm ?: "",
    PAYATNM = this.payatnm ?: "",
    PLACENM = this.placenm ?: "",
    RCPTBGNDT = this.rcptbgndt ?: "",
    RCPTENDDT = this.rcptenddt ?: "",
    REVSTDDAY = (this.revstdday ?: 0).toString(),
    REVSTDDAYNM = this.revstddaynm ?: "",
    SVCID = this.svcid ?: "",
    SVCNM = this.svcnm ?: "",
    SVCOPNBGNDT = this.svcopnbgndt ?: "",
    SVCOPNENDDT = this.svcopnenddt ?: "",
    SVCSTATNM = this.svcstatnm ?: "",
    SVCURL = this.svcurl ?: "",
    TELNO = this.telno ?: "",
    USETGTINFO = this.usetgtinfo ?: "",
    V_MAX = this.vMax ?: "",
    V_MIN = this.vMin ?: "",
    X = this.x ?: "",
    Y = this.y ?: "",
)

//fun Collection<ReservationEntity>.toRowList() = this.map { it.toRow() }
fun Collection<Row>.toReservationEntityList() = this.map { it.toReservationEntity() }


//object RoomRowMapper {
//    /**
//     * @property mappingRoomToRow Room에서 사용하는 [ReservationEntity] 타입을 API에서 사용하는 [Row] 타입으로 변환
//     * @param reservationEntity Room에서 사용하는 [ReservationEntity]타입
//     * @return API값을 받아오는 [Row]타입
//     */
//    fun mappingRoomToRow(reservationEntity: ReservationEntity): Row {
//        return Row(
//            div = "",
//            service = "",
//            gubun = reservationEntity.GUBUN,
//            svcid = reservationEntity.SVCID,
//            maxclassnm = reservationEntity.MAXCLASSNM,
//            minclassnm = reservationEntity.MINCLASSNM,
//            svcstatnm = reservationEntity.SVCSTATNM,
//            svcnm = reservationEntity.SVCNM,
//            payatnm = reservationEntity.PAYATNM,
//            placenm = reservationEntity.PLACENM,
//            usetgtinfo = reservationEntity.USETGTINFO,
//            svcurl = reservationEntity.SVCURL,
//            x = reservationEntity.X,
//            y = reservationEntity.Y,
//            svcopnbgndt = reservationEntity.SVCOPNBGNDT,
//            svcopnenddt = reservationEntity.SVCOPNENDDT,
//            rcptbgndt = reservationEntity.RCPTBGNDT,
//            rcptenddt = reservationEntity.RCPTENDDT,
//            areanm = reservationEntity.AREANM,
//            imgurl = reservationEntity.IMGURL,
//            dtlcont = reservationEntity.DTLCONT,
//            telno = reservationEntity.TELNO,
//            vMax = reservationEntity.V_MAX,
//            vMin = reservationEntity.V_MIN,
//            revstdday = reservationEntity.REVSTDDAY.toLong(),
//            revstddaynm = reservationEntity.REVSTDDAYNM
//        )
//    }
//
//    /**
//     * @property mappingRoomToRow [ReservationEntity] 타입의 List를 [Row] 타입의 List로 변환
//     * @param reservationEntities [ReservationEntity]타입의 List
//     * @return [Row]타입의 List
//     */
//    fun mappingRoomToRow(reservationEntities: Collection<ReservationEntity>): List<Row> =
//        reservationEntities.map { mappingRoomToRow(it) }
//
//    /**
//     * @property mappingRowToRoom API값을 받는 [Row] 타입을 Room에서 사용하는 [ReservationEntity] 타입으로 변환
//     * @param row API값을 받는 [Row] 타입
//     * @return Room에서 사용하는 [ReservationEntity] 타입
//     */
//    fun mappingRowToRoom(row: Row): ReservationEntity {
//        return ReservationEntity(
//            AREANM = row.areanm,
//            DTLCONT = row.dtlcont,
//            GUBUN = row.gubun,
//            IMGURL = row.imgurl,
//            MAXCLASSNM = row.maxclassnm,
//            MINCLASSNM = row.minclassnm,
//            PAYATNM = row.payatnm,
//            PLACENM = row.placenm,
//            RCPTBGNDT = row.rcptbgndt,
//            RCPTENDDT = row.rcptenddt,
//            REVSTDDAY = row.revstdday.toString(),
//            REVSTDDAYNM = row.revstddaynm,
//            SVCID = row.svcid,
//            SVCNM = row.svcnm,
//            SVCOPNBGNDT = row.svcopnbgndt,
//            SVCOPNENDDT = row.svcopnenddt,
//            SVCSTATNM = row.svcstatnm,
//            SVCURL = row.svcurl,
//            TELNO = row.telno,
//            USETGTINFO = row.usetgtinfo,
//            V_MAX = row.vMax,
//            V_MIN = row.vMin,
//            X = row.x,
//            Y = row.y
//        )
//    }
//
//    /**
//     * @property mappingRowToRoom [Row] 타입의 List를 [ReservationEntity] 타입의 List로 변환
//     * @param rows [Row]타입의 List
//     * @return [ReservationEntity]타입의 List
//     */
//    fun mappingRowToRoom(rows: Collection<Row>): List<ReservationEntity> =
//        rows.map { mappingRowToRoom(it) }
//}
