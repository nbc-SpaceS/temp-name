package com.wannabeinseoul.seoulpublicservice.weather

import java.time.LocalDateTime

data class LocalDateTimeParser (
    val year: Int = LocalDateTime.now().year,
    val month: Int = String.format("%02d",LocalDateTime.now().monthValue).toInt(),
    val day: Int = LocalDateTime.now().dayOfMonth,
    val hour: Int = LocalDateTime.now().hour
)