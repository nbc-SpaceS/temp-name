package com.wannabeinseoul.seoulpublicservice.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.load
import com.wannabeinseoul.seoulpublicservice.R

fun ImageView.loadWithHolder(
    data: Any?,
    @DrawableRes placeholderResId: Int = R.drawable.ic_loading,
    @DrawableRes errorResId: Int = R.drawable.place_holder_1,
) =
    this.load(data) {
        crossfade(true)
        placeholder(placeholderResId)
        error(errorResId)
    }
