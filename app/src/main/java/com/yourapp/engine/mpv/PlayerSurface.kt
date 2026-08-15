package com.yourapp.engine.mpv

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.AttributeSet
import `is`.xyz.mpv.MPVLib
import com.yourapp.domain.DecoderMode
import com.yourapp.domain.PlaybackState
import com.yourapp.domain.PlayerEngine
import com.yourapp.domain.PlayerEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class PlayerSurface(
    context: Context,
    attrs: AttributeSet
) : MpvEngine(context, attrs), PlayerEngine {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val dispatcher = MpvEventDispatcher(scope)
    private var currentVideoWidth = 0
    private var currentVideoHeight = 0

    init {
        dispatcher.register()
    }

    override val events: Flow<PlayerEvent> = dispatcher.events.mapNotNull { event ->
        when (event) {
            is MpvEvent.Pause -> PlayerEvent.PlaybackStateChanged(if (event.paused) PlaybackState.Paused else PlaybackState.Playing)
            is MpvEvent.TimePos -> PlayerEvent.PositionChanged((event.seconds * 1000).toLong())
            is MpvEvent.Duration -> PlayerEvent.DurationChanged((event.seconds * 1000).toLong())
            is MpvEvent.EofReached -> PlayerEvent.PlaybackStateChanged(PlaybackState.Ended)
            is MpvEvent.BufferingChanged -> null
            is MpvEvent.TrackListChanged -> null
            is MpvEvent.VideoWidth -> {
                currentVideoWidth = event.width
                PlayerEvent.VideoSizeChanged(currentVideoWidth, currentVideoHeight)
            }
            is MpvEvent.VideoHeight -> {
                currentVideoHeight = event.height
                PlayerEvent.VideoSizeChanged(currentVideoWidth, currentVideoHeight)
            }
        }
    }

    override fun play() {
        MPVLib.setPropertyBoolean("pause", false)
    }

    override fun pause() {
        MPVLib.setPropertyBoolean("pause", true)
    }

    override fun seekTo(positionMs: Long) {
        val seconds = positionMs / 1000.0
        MPVLib.command("seek", seconds.toString(), "absolute")
    }

    override fun setAudioTrack(id: Int) {
        MPVLib.setPropertyInt("aid", id)
    }

    override fun setSubtitleTrack(id: Int) {
        MPVLib.setPropertyInt("sid", id)
    }

    override fun setSpeed(speed: Float) {
        MPVLib.setPropertyDouble("speed", speed.toDouble())
    }

    override fun setDecoder(mode: DecoderMode) {
        val value = when (mode) {
            DecoderMode.HW_PLUS -> "mediacodec,mediacodec-copy,no"
            DecoderMode.HW      -> "mediacodec-copy,no"
            DecoderMode.SW      -> "no"
        }
        MPVLib.setOptionString("hwdec", value)
    }

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
        MPVLib.observeProperty("video-params/w", MPVLib.MpvFormat.MPV_FORMAT_INT64)
        MPVLib.observeProperty("video-params/h", MPVLib.MpvFormat.MPV_FORMAT_INT64)
    }
}
