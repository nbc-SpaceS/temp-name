package com.wannabeinseoul.seoulpublicservice.db_by_memory

import com.wannabeinseoul.seoulpublicservice.seoul.Row

private val areasInSeoul = listOf(
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
    fun getAll(): List<Row>
    fun getHaveLocation(): List<Row>
    fun getFiltered(
        minclassnm: List<String>? = null,
        areanm: List<String>? = null,
        svcstatnm: List<String>? = null,
        payatnm: List<String>? = null
    ): List<Row>

    fun findBySvcid(svcid: String): Row?
}

class DbMemoryRepositoryImpl(private val getAppRowList: () -> List<Row>) : DbMemoryRepository {

    override fun getAll() = getAppRowList()

    override fun getHaveLocation() = getAppRowList().getHaveLocation()

    override fun getFiltered(
        minclassnm: List<String>?,
        areanm: List<String>?,
        svcstatnm: List<String>?,
        payatnm: List<String>?
    ): List<Row> {
        return getHaveLocation().getFiltered(minclassnm, areanm, svcstatnm, payatnm)
    }

    override fun findBySvcid(svcid: String) = getAppRowList().find { it.svcid == svcid }

}

/* 다른 곳들에서도 사용 가능한 확장함수 */
fun Row.isInSeoul() = this.areanm in areasInSeoul
fun Row.isNotInSeoul() = this.areanm !in areasInSeoul

fun List<Row>.getInSeoul() = this.filter { it.isInSeoul() }
//fun List<Row?>.getInSeoul() = this.filter { it?.isInSeoul() ?: false } as List<Row>

fun List<Row>.getNotInSeoul() = this.filter { it.isNotInSeoul() }
//fun List<Row?>.getNotInSeoul() = this.filter { it?.isNotInSeoul() ?: false } as List<Row>

fun List<Row>.getHaveLocation() = this.filter { it.x.isBlank().not() && it.y.isBlank().not() }
//fun List<Row?>.getHaveLocation() =
//    this.filter { it != null && it.x.isBlank().not() && it.y.isBlank().not() } as List<Row>

fun List<Row>.getFiltered(
    minclassnm: List<String>?,
    areanm: List<String>?,
    svcstatnm: List<String>?,
    payatnm: List<String>?
): List<Row> {
    return if (areanm?.any { it == "시외" || it == "서울제외지역" } == true) {
        getHaveLocation().filter {
            (minclassnm.isNullOrEmpty() || it.minclassnm in minclassnm) &&
                    (areanm.isEmpty() || it.areanm in areanm || it.isNotInSeoul()) &&
                    (svcstatnm.isNullOrEmpty() || it.svcstatnm in svcstatnm) &&
                    (payatnm.isNullOrEmpty() || it.payatnm in payatnm)
        }
    } else {
        getHaveLocation().filter {
            (minclassnm.isNullOrEmpty() || it.minclassnm in minclassnm) &&
                    (areanm.isNullOrEmpty() || it.areanm in areanm) &&
                    (svcstatnm.isNullOrEmpty() || it.svcstatnm in svcstatnm) &&
                    (payatnm.isNullOrEmpty() || it.payatnm in payatnm)
        }
    }
}

//fun List<Row?>.getFiltered(
//    minclassnm: List<String>?,
//    areanm: List<String>?,
//    svcstatnm: List<String>?,
//    payatnm: List<String>?
//) = this.filterNotNull().getFiltered(minclassnm, areanm, svcstatnm, payatnm)
