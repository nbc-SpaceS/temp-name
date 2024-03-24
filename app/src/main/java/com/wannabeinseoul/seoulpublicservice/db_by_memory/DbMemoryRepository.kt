package com.wannabeinseoul.seoulpublicservice.db_by_memory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val areasInSeoul = setOf(
    "강남구",
    "강동구",
    "강북구",
    "강서구",
    "관악구",
    "광진구",
    "구로구",
    "금천구",
    "노원구",
    "도봉구",
    "동대문구",
    "동작구",
    "마포구",
    "서대문구",
    "서초구",
    "성동구",
    "성북구",
    "송파구",
    "양천구",
    "영등포구",
    "용산구",
    "은평구",
    "종로구",
    "중구",
    "중랑구"
)

interface DbMemoryRepository {
    fun getAll(): List<ReservationEntity>
    fun setAll(reservationEntityList: List<ReservationEntity>)
    fun postAll(reservationEntityList: List<ReservationEntity>)
    fun getHasLocation(): List<ReservationEntity>
    fun getFiltered(
        minclassnm: List<String>? = null,
        areanm: List<String>? = null,
        svcstatnm: List<String>? = null,
        payatnm: List<String>? = null,
        usetgtinfo: List<String>? = null
    ): List<ReservationEntity>

    fun getFilteredPlusWord(
        word: String,
        minclassnm: List<String>? = null,
        areanm: List<String>? = null,
        svcstatnm: List<String>? = null,
        payatnm: List<String>? = null
    ): List<ReservationEntity>

    fun getFilteredByDate(): List<String>

    fun getFilteredCountWithMaxClass(
        maxclassnm: List<String>,
        areanm: String
    ): List<Pair<String, Int>>

    fun findBySvcid(svcid: String): ReservationEntity?
}

class DbMemoryRepositoryImpl : DbMemoryRepository {

    private val _entityListLd = MutableLiveData<List<ReservationEntity>>(emptyList())
    private val entityListLd: LiveData<List<ReservationEntity>> get() = _entityListLd

    override fun getAll() = entityListLd.value!!
    override fun setAll(reservationEntityList: List<ReservationEntity>) {
        _entityListLd.value = reservationEntityList
    }

    override fun postAll(reservationEntityList: List<ReservationEntity>) =
        _entityListLd.postValue(reservationEntityList)

    override fun getHasLocation() = getAll().getHasLocation()

    override fun getFiltered(
        minclassnm: List<String>?,
        areanm: List<String>?,
        svcstatnm: List<String>?,
        payatnm: List<String>?,
        usetgtinfo: List<String>?
    ): List<ReservationEntity> {
        return getHasLocation()
            .getFiltered(minclassnm, areanm, svcstatnm, payatnm, usetgtinfo)
    }

    override fun getFilteredPlusWord(
        word: String,
        minclassnm: List<String>?,
        areanm: List<String>?,
        svcstatnm: List<String>?,
        payatnm: List<String>?
    ): List<ReservationEntity> {
        return getHasLocation()
            .getFilteredPlusWord(word, minclassnm, areanm, svcstatnm, payatnm)
    }

    override fun getFilteredByDate(): List<String> {
        return getHasLocation().getFilteredByDate()
    }

    override fun getFilteredCountWithMaxClass(
        maxclassnm: List<String>,
        areanm: String
    ): List<Pair<String, Int>> {
        return getHasLocation()
            .getFilteredCountWithMaxClass(maxclassnm, areanm)
    }

    override fun findBySvcid(svcid: String) = getAll().find { it.SVCID == svcid }

}

/* 다른 곳들에서도 사용 가능한 확장함수 */
fun ReservationEntity.isInSeoul() = this.AREANM in areasInSeoul
fun ReservationEntity.isNotInSeoul() = this.AREANM !in areasInSeoul
fun ReservationEntity.hasLocation() =
    this.X.toDoubleOrNull() != null && this.Y.toDoubleOrNull() != null

fun List<ReservationEntity>.getInSeoul() = this.filter { it.isInSeoul() }
fun List<ReservationEntity>.getNotInSeoul() = this.filter { it.isNotInSeoul() }
fun List<ReservationEntity>.getHasLocation() = this.filter { it.hasLocation() }
fun List<ReservationEntity>.getFiltered(
    minclassnm: List<String>?,
    areanm: List<String>?,
    svcstatnm: List<String>?,
    payatnm: List<String>?,
    usetgtinfo: List<String>?
): List<ReservationEntity> {
//    Log.d(
//        "jj-DbMemoryRepositoryImpl",
//        "$minclassnm\n" +
//                "$areanm\n" +
//                "$svcstatnm\n" +
//                "$payatnm"
//    )
    return if (areanm?.any { it == "시외" || it == "서울제외지역" } == true) {
        getHasLocation().filter {
            (minclassnm.isNullOrEmpty() || it.MINCLASSNM in minclassnm) &&
                    (areanm.isEmpty() || it.AREANM.isNotBlank() && (it.AREANM in areanm || it.isNotInSeoul())) &&
                    (svcstatnm.isNullOrEmpty() || it.SVCSTATNM in svcstatnm) &&
                    (payatnm.isNullOrEmpty() || it.PAYATNM in payatnm) &&
                    (usetgtinfo.isNullOrEmpty() || it.USETGTINFO in usetgtinfo)
        }
    } else {
        getHasLocation().filter {
            (minclassnm.isNullOrEmpty() || it.MINCLASSNM in minclassnm) &&
                    (areanm.isNullOrEmpty() || it.AREANM in areanm) &&
                    (svcstatnm.isNullOrEmpty() || it.SVCSTATNM in svcstatnm) &&
                    (payatnm.isNullOrEmpty() || it.PAYATNM in payatnm) &&
                    (usetgtinfo.isNullOrEmpty() || it.USETGTINFO in usetgtinfo)
        }
    }
}

fun List<ReservationEntity>.getFilteredPlusWord(
    word: String,
    minclassnm: List<String>?,
    areanm: List<String>?,
    svcstatnm: List<String>?,
    payatnm: List<String>?
): List<ReservationEntity> {
    return if (areanm?.any { it == "시외" || it == "서울제외지역" } == true) {
        getHasLocation().filter {
            (it.SVCNM.contains(word) || it.PLACENM.contains(word) || it.AREANM.contains(word)
                    || it.TELNO.contains(word) || it.MINCLASSNM.contains(word) || it.USETGTINFO.contains(
                word
            )) &&
                    (minclassnm.isNullOrEmpty() || it.MINCLASSNM in minclassnm) &&
                    (areanm.isEmpty() || it.AREANM.isNotBlank() && (it.AREANM in areanm || it.isNotInSeoul())) &&
                    (svcstatnm.isNullOrEmpty() || it.SVCSTATNM in svcstatnm) &&
                    (payatnm.isNullOrEmpty() || it.PAYATNM in payatnm)
        }
    } else {
        getHasLocation().filter {
            (it.SVCNM.contains(word) || it.PLACENM.contains(word) || it.AREANM.contains(word)
                    || it.TELNO.contains(word) || it.MINCLASSNM.contains(word) || it.USETGTINFO.contains(
                word
            )) &&
                    (minclassnm.isNullOrEmpty() || it.MINCLASSNM in minclassnm) &&
                    (areanm.isNullOrEmpty() || it.AREANM in areanm) &&
                    (svcstatnm.isNullOrEmpty() || it.SVCSTATNM in svcstatnm) &&
                    (payatnm.isNullOrEmpty() || it.PAYATNM in payatnm)
        }
    }
}

fun List<ReservationEntity>.getFilteredByDate(): List<String> {
    val datePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")
    return getHasLocation().filter {
        (it.SVCNM == "접수중") &&
                (datePattern.format(
                    LocalDateTime.parse(
                        it.RCPTBGNDT,
                        formatter
                    )
                ) >= LocalDateTime.now().minusDays(3).format(datePattern) && datePattern.format(
                    LocalDateTime.parse(
                        it.RCPTBGNDT,
                        formatter
                    )
                ) <= LocalDateTime.now().plusDays(3).format(datePattern))
    }.map { it.SVCID }
}

fun List<ReservationEntity>.getFilteredCountWithMaxClass(
    maxclassnm: List<String>,
    areanm: String
): List<Pair<String, Int>> {
    return maxclassnm.map { maxClass ->
        Pair(maxClass, getHasLocation().count { data ->
            (data.MAXCLASSNM == maxClass) &&
                    (data.AREANM == areanm)
        })
    }
}


//fun Row.isInSeoul() = this.areanm in areasInSeoul
//fun Row.isNotInSeoul() = this.areanm !in areasInSeoul
//fun Row.hasLocation() = this.x.toDoubleOrNull() != null && this.y.toDoubleOrNull() != null
//fun List<Row>.getInSeoul() = this.filter { it.isInSeoul() }
//fun List<Row>.getNotInSeoul() = this.filter { it.isNotInSeoul() }
//fun List<Row>.getHasLocation() = this.filter { it.hasLocation() }
//fun List<Row>.getFiltered(
//    minclassnm: List<String>?,
//    areanm: List<String>?,
//    svcstatnm: List<String>?,
//    payatnm: List<String>?,
//    usetgtinfo: List<String>?
//): List<Row> {
////    Log.d(
////        "jj-DbMemoryRepositoryImpl",
////        "$minclassnm\n" +
////                "$areanm\n" +
////                "$svcstatnm\n" +
////                "$payatnm"
////    )
//    return if (areanm?.any { it == "시외" || it == "서울제외지역" } == true) {
//        getHasLocation().filter {
//            (minclassnm.isNullOrEmpty() || it.minclassnm in minclassnm) &&
//                    (areanm.isEmpty() || it.areanm.isNotBlank() && (it.areanm in areanm || it.isNotInSeoul())) &&
//                    (svcstatnm.isNullOrEmpty() || it.svcstatnm in svcstatnm) &&
//                    (payatnm.isNullOrEmpty() || it.payatnm in payatnm) &&
//                    (usetgtinfo.isNullOrEmpty() || it.usetgtinfo in usetgtinfo)
//        }
//    } else {
//        getHasLocation().filter {
//            (minclassnm.isNullOrEmpty() || it.minclassnm in minclassnm) &&
//                    (areanm.isNullOrEmpty() || it.areanm in areanm) &&
//                    (svcstatnm.isNullOrEmpty() || it.svcstatnm in svcstatnm) &&
//                    (payatnm.isNullOrEmpty() || it.payatnm in payatnm) &&
//                    (usetgtinfo.isNullOrEmpty() || it.usetgtinfo in usetgtinfo)
//        }
//    }
//}
//
//fun List<Row>.getFilteredPlusWord(
//    word: String,
//    minclassnm: List<String>?,
//    areanm: List<String>?,
//    svcstatnm: List<String>?,
//    payatnm: List<String>?
//): List<Row> {
//    return if (areanm?.any { it == "시외" || it == "서울제외지역" } == true) {
//        getHasLocation().filter {
//            (it.svcnm.contains(word) || it.placenm.contains(word) || it.areanm.contains(word)
//                    || it.telno.contains(word) || it.minclassnm.contains(word) || it.usetgtinfo.contains(
//                word
//            )) &&
//                    (minclassnm.isNullOrEmpty() || it.minclassnm in minclassnm) &&
//                    (areanm.isEmpty() || it.areanm.isNotBlank() && (it.areanm in areanm || it.isNotInSeoul())) &&
//                    (svcstatnm.isNullOrEmpty() || it.svcstatnm in svcstatnm) &&
//                    (payatnm.isNullOrEmpty() || it.payatnm in payatnm)
//        }
//    } else {
//        getHasLocation().filter {
//            (it.svcnm.contains(word) || it.placenm.contains(word) || it.areanm.contains(word)
//                    || it.telno.contains(word) || it.minclassnm.contains(word) || it.usetgtinfo.contains(
//                word
//            )) &&
//                    (minclassnm.isNullOrEmpty() || it.minclassnm in minclassnm) &&
//                    (areanm.isNullOrEmpty() || it.areanm in areanm) &&
//                    (svcstatnm.isNullOrEmpty() || it.svcstatnm in svcstatnm) &&
//                    (payatnm.isNullOrEmpty() || it.payatnm in payatnm)
//        }
//    }
//}
//
//fun List<Row>.getFilteredByDate(): List<String> {
//    val datePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")
//    return getHasLocation().filter {
//        (it.svcstatnm == "접수중") &&
//                (datePattern.format(
//                    LocalDateTime.parse(
//                        it.rcptbgndt,
//                        formatter
//                    )
//                ) >= LocalDateTime.now().minusDays(3).format(datePattern) && datePattern.format(
//                    LocalDateTime.parse(
//                        it.rcptbgndt,
//                        formatter
//                    )
//                ) <= LocalDateTime.now().plusDays(3).format(datePattern))
//    }.map { it.svcid }
//}
//
//fun List<Row>.getFilteredCountWithMaxClass(
//    maxclassnm: List<String>,
//    areanm: String
//): List<Pair<String, Int>> {
//    return maxclassnm.map { maxClass ->
//        Pair(maxClass, getHasLocation().count { data ->
//            (data.maxclassnm == maxClass) &&
//                    (data.areanm == areanm)
//        })
//    }
//}
