package com.wannabeinseoul.seoulpublicservice.util

import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.load
import coil.request.ErrorResult
import coil.request.ImageRequest
import com.wannabeinseoul.seoulpublicservice.BuildConfig
import com.wannabeinseoul.seoulpublicservice.R

//fun ImageView.loadWithHolder(
//    data: Any?,
//    @DrawableRes placeholderResId: Int = R.drawable.ic_loading,
//    @DrawableRes errorResId: Int = R.drawable.place_holder_1,
//) =
//    this.load(data) {
//        crossfade(true)
//        placeholder(placeholderResId)
//        error(errorResId)
//    }

fun ImageView.loadWithHolder(
    data: Any?,
    @DrawableRes placeholderResId: Int = R.drawable.ic_loading,
    @DrawableRes errorResId: Int = R.drawable.place_holder_1,
) =
    this.load(data) {
        val JJTAG = "jj-ImageView.loadWithHolder"
        val scaleType = this@loadWithHolder.scaleType
        crossfade(true)
        if (BuildConfig.DEBUG) {
            listener(onError = { request: ImageRequest, result: ErrorResult ->
                Log.w(JJTAG, "$this request.data: ${request.data}", result.throwable)
            })
        }
        target(
            onStart = {
                this@loadWithHolder.scaleType = ImageView.ScaleType.FIT_CENTER
                this@loadWithHolder.setImageResource(placeholderResId)
            },
            onError = {
                this@loadWithHolder.scaleType = scaleType
                this@loadWithHolder.setImageResource(errorResId)
            },
            onSuccess = {
                this@loadWithHolder.scaleType = scaleType
                this@loadWithHolder.setImageDrawable(it)
            }
        )
    }
