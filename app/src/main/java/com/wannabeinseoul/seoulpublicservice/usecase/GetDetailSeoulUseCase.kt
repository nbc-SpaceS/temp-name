package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.pref.PrefRepository
import com.wannabeinseoul.seoulpublicservice.seoul.DetailRow
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepository
import com.google.gson.Gson

class GetDetailSeoulUseCase(
    private val seoulPublicRepository: SeoulPublicRepository,
    private val prefRepository: PrefRepository
) {

    private val keyDetail = "keyDetail"  //ddd
    private val gson = Gson()

    suspend operator fun invoke(svcId: String): DetailRow? {
        val item = seoulPublicRepository.getDetail(svcId) ?: return null
        prefRepository.save(keyDetail + svcId, gson.toJson(item))  //ddd
        return item
    }

}
