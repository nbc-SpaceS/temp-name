package com.wannabeinseoul.seoulpublicservice.util

import android.util.Log
import com.wannabeinseoul.seoulpublicservice.BuildConfig

object DLog {
    private val debug = BuildConfig.DEBUG

    fun d(tag: String?, msg: String) {
        if (debug) Log.d(tag, msg)
    }

    fun d(tag: String?, msg: String?, tr: Throwable?) {
        if (debug) Log.d(tag, msg, tr)
    }

    fun v(tag: String?, msg: String) {
        if (debug) Log.v(tag, msg)
    }

    fun v(tag: String?, msg: String?, tr: Throwable?) {
        if (debug) Log.v(tag, msg, tr)
    }

    fun i(tag: String?, msg: String) {
        if (debug) Log.i(tag, msg)
    }

    fun i(tag: String?, msg: String?, tr: Throwable?) {
        if (debug) Log.i(tag, msg, tr)
    }

    fun e(tag: String?, msg: String) {
        if (debug) Log.e(tag, msg)
    }

    fun e(tag: String?, msg: String?, tr: Throwable?) {
        if (debug) Log.e(tag, msg, tr)
    }

    fun w(tag: String?, msg: String) {
        if (debug) Log.w(tag, msg)
    }

    fun w(tag: String?, tr: Throwable?) {
        if (debug) Log.w(tag, tr)
    }

    fun w(tag: String?, msg: String?, tr: Throwable?) {
        if (debug) Log.w(tag, msg, tr)
    }

}
