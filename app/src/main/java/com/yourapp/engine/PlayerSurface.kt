package com.yourapp.engine

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.AttributeSet
import `is`.xyz.mpv.MPVLib

class PlayerSurface(
    context: Context,
    attrs: AttributeSet
) : MpvEngine(context, attrs) {


    override fun initOptions() {
        MPVLib.setOptionString("vo", "gpu")
        MPVLib.setOptionString("hwdec", "mediacodec,mediacodec-copy,no")
        MPVLib.setOptionString("hwdec-codecs", "all")
        MPVLib.setOptionString("keep-open", "yes")
        
        val maxBytes = if (Build.VERSION.SDK_INT < 27) {
            "32MiB"
        } else {
            "64MiB"
        }
        MPVLib.setOptionString("demuxer-max-bytes", maxBytes)
        MPVLib.setOptionString("demuxer-max-back-bytes", maxBytes)
        MPVLib.setOptionString("hr-seek", "yes")
        
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
        MPVLib.setOptionString("screenshot-directory", picturesDir)
    }

    override fun postInitOptions() {
        // Implementation for postInitOptions if needed, or leave blank
    }

    override fun observeProperties() {
        MPVLib.observeProperty("pause", MPVLib.MpvFormat.MPV_FORMAT_FLAG)
        MPVLib.observeProperty("eof-reached", MPVLib.MpvFormat.MPV_FORMAT_FLAG)
        MPVLib.observeProperty("time-pos", MPVLib.MpvFormat.MPV_FORMAT_DOUBLE)
        MPVLib.observeProperty("duration", MPVLib.MpvFormat.MPV_FORMAT_DOUBLE)
        MPVLib.observeProperty("paused-for-cache", MPVLib.MpvFormat.MPV_FORMAT_FLAG)
        MPVLib.observeProperty("track-list", MPVLib.MpvFormat.MPV_FORMAT_STRING)
        MPVLib.observeProperty("sid", MPVLib.MpvFormat.MPV_FORMAT_STRING)
        MPVLib.observeProperty("aid", MPVLib.MpvFormat.MPV_FORMAT_STRING)
    }
}
