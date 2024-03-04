package com.example.seoulpublicservice.util

import android.widget.ImageView
import coil.load
import com.example.seoulpublicservice.R

fun ImageView.loadWithHolder(data: Any?) =
    this.load(data) {
        crossfade(true)
        placeholder(R.drawable.ic_loading)
        error(R.drawable.place_holder_1)
    }
