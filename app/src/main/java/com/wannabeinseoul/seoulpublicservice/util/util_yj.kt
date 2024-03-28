package com.wannabeinseoul.seoulpublicservice.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.Toast
import androidx.core.text.HtmlCompat
import java.io.ByteArrayOutputStream

fun String.fromHtml(flags: Int = HtmlCompat.FROM_HTML_MODE_LEGACY) =
    HtmlCompat.fromHtml(this, flags)

fun String.parseColor() = try {
    Color.parseColor(this)
} catch (e: Throwable) {
    DLog.e("jj-String.parseColor", "parseColor failed: $this", e)
    0
}

fun toastShort(context: Context, text: CharSequence) =
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

fun toastLong(context: Context, text: CharSequence) =
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()

fun Bitmap.toPngByteArray(): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}
