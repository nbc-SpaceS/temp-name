package com.example.seoulpublicservice.util

fun String.trimUpTo(length: Int) = if (this.length <= length) this else this.substring(0, length)
