package com.yourapp.domain

import android.text.format.DateUtils

object TimeFormatter {
    fun format(ms: Long): String {
        return DateUtils.formatElapsedTime(ms / 1000)
    }
}
