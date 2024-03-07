package com.wannabeinseoul.seoulpublicservice.util

import androidx.core.text.HtmlCompat

fun String.fromHtml(flags: Int = HtmlCompat.FROM_HTML_MODE_LEGACY) =
    HtmlCompat.fromHtml(this, flags)
