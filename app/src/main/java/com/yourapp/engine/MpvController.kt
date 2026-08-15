package com.yourapp.engine

import `is`.xyz.mpv.MPVLib
import com.yourapp.domain.DecoderMode

class MpvController {

    // Note: All MPVLib calls must be on the main thread

    fun loadFile(path: String) {
        MPVLib.command("loadfile", path)
    }

    fun play() {
        MPVLib.setPropertyBoolean("pause", false)
    }

    fun pause() {
        MPVLib.setPropertyBoolean("pause", true)
    }

    fun seekTo(seconds: Double) {
        MPVLib.command("seek", seconds.toString(), "absolute")
    }

    fun seekBy(seconds: Double) {
        MPVLib.command("seek", seconds.toString(), "relative")
    }

    fun setTrack(type: String, id: Int) {
        MPVLib.setPropertyInt(type, id)
    }

    fun getCurrentPosition(): Double {
        return MPVLib.getPropertyDouble("time-pos") ?: 0.0
    }

    fun getDuration(): Double {
        return MPVLib.getPropertyDouble("duration") ?: 0.0
    }

    fun stop() {
        MPVLib.command("stop")
    }

    // Must be called on main thread
    fun setSpeed(speed: Float) {
        MPVLib.setPropertyDouble("speed", speed.toDouble())
    }

    fun setDecoder(mode: DecoderMode) {
        val value = when (mode) {
            DecoderMode.HW_PLUS -> "mediacodec,mediacodec-copy,no"
            DecoderMode.HW      -> "mediacodec-copy,no"
            DecoderMode.SW      -> "no"
        }
        MPVLib.setOptionString("hwdec", value)
    }
}
