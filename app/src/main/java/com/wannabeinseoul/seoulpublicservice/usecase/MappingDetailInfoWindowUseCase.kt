package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.pref.SavedPrefRepository
import com.wannabeinseoul.seoulpublicservice.seoul.Row
import com.wannabeinseoul.seoulpublicservice.ui.map.DetailInfoWindow

class MappingDetailInfoWindowUseCase(
    private val savedPrefRepository: SavedPrefRepository
) {
    operator fun invoke(serviceInfoList: List<Row>): List<DetailInfoWindow> = serviceInfoList.map {
        DetailInfoWindow(
            svcid = it.svcid,
            imgurl = it.imgurl,
            areanm = it.areanm,
            svcnm = it.svcnm,
            payatnm = it.payatnm,
            svcstatnm = it.svcstatnm,
            svcurl = it.svcurl,
            saved = savedPrefRepository.contains(it.svcid)
        )
    }
}
