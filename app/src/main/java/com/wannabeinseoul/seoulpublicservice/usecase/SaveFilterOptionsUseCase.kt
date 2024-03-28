package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.pref.FilterPrefRepository

class SaveFilterOptionsUseCase(
    private val filterPrefRepository: FilterPrefRepository
) {
    operator fun invoke(options: List<MutableList<String>>) {
        filterPrefRepository.save(options)
    }
}
