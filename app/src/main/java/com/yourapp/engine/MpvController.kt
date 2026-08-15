package com.yourapp.engine

import `is`.xyz.mpv.MPVLib
import com.yourapp.domain.DecoderMode

class MpvController {

    fun setDecoder(mode: DecoderMode) {
        val value = when (mode) {
            DecoderMode.HW_PLUS -> "mediacodec,mediacodec-copy,no"
            DecoderMode.HW      -> "mediacodec-copy,no"
            DecoderMode.SW      -> "no"
        }
        MPVLib.setOptionString("hwdec", value)
    }
}
