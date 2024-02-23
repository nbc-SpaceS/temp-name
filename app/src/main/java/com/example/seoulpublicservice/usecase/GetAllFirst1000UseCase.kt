package com.example.seoulpublicservice.usecase

import com.example.seoulpublicservice.seoul.Row
import com.example.seoulpublicservice.seoul.SeoulPublicRepository

class GetAllFirst1000UseCase(
    private val seoulPublicRepository: SeoulPublicRepository
) {
    suspend operator fun invoke(): List<Row> = seoulPublicRepository.getAllFirst1000()
}
