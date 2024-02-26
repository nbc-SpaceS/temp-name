package com.example.seoulpublicservice.usecase

import com.example.seoulpublicservice.pref.PrefRepository
import com.example.seoulpublicservice.seoul.DetailRow
import com.example.seoulpublicservice.seoul.SeoulPublicRepository
import com.google.gson.Gson

class GetDetailSeoulUseCase(
    private val seoulPublicRepository: SeoulPublicRepository,
    private val prefRepository: PrefRepository
) {

    private val keyDetail = "keyDetail"  //ddd
    private val gson = Gson()

    suspend operator fun invoke(svcid: String): DetailRow? {
        val item = seoulPublicRepository.getDetail(svcid) ?: return null
        prefRepository.save(keyDetail + svcid, gson.toJson(item))  //ddd
        return item
    }

}
