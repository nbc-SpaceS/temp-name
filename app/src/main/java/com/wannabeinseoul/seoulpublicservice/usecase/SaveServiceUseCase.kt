package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.pref.SavedPrefRepository

class SaveServiceUseCase(
    private val savedPrefRepository: SavedPrefRepository
) {
    operator fun invoke(svcId: String) {
        if (savedPrefRepository.contains(svcId)) {
            savedPrefRepository.remove(svcId)
        } else {
            savedPrefRepository.addSvcid(svcId)
        }
    }
}
