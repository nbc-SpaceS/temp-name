package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.pref.FilterPrefRepository

class LoadSavedFilterOptionsUseCase(
    private val filterPrefRepository: FilterPrefRepository
) {
    operator fun invoke(): List<List<String>> = filterPrefRepository.load()
}
