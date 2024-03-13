package com.wannabeinseoul.seoulpublicservice.util

import android.content.Context
import android.graphics.Color
import android.widget.Toast
import androidx.core.text.HtmlCompat

fun String.fromHtml(flags: Int = HtmlCompat.FROM_HTML_MODE_LEGACY) =
    HtmlCompat.fromHtml(this, flags)

fun String.parseColor() = Color.parseColor(this)

fun toastShort(context: Context, text: CharSequence) =
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

fun toastLong(context: Context, text: CharSequence) =
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
