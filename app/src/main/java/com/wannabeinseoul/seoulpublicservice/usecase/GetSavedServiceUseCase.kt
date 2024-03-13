package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.pref.SavedPrefRepository

class GetSavedServiceUseCase(
    private val savedPrefRepository: SavedPrefRepository
) {
    operator fun invoke() = savedPrefRepository
}
