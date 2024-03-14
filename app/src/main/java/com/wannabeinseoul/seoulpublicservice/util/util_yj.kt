package com.wannabeinseoul.seoulpublicservice.util

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.core.text.HtmlCompat

fun String.fromHtml(flags: Int = HtmlCompat.FROM_HTML_MODE_LEGACY) =
    HtmlCompat.fromHtml(this, flags)

fun String.parseColor() = try {
    Color.parseColor(this)
} catch (e: Throwable) {
    Log.e("jj-String.parseColor", "parseColor failed: $e")
    0
}

fun toastShort(context: Context, text: CharSequence) =
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

fun toastLong(context: Context, text: CharSequence) =
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
