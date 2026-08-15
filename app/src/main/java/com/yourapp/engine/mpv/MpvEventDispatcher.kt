package com.yourapp.engine.mpv

import `is`.xyz.mpv.MPVLib
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class MpvEvent {
    data class Pause(val paused: Boolean) : MpvEvent()
    data class TimePos(val seconds: Double) : MpvEvent()
    data class Duration(val seconds: Double) : MpvEvent()
    object EofReached : MpvEvent()
    data class BufferingChanged(val isBuffering: Boolean) : MpvEvent()
    object TrackListChanged : MpvEvent()
    data class VideoWidth(val width: Int) : MpvEvent()
    data class VideoHeight(val height: Int) : MpvEvent()
}

class MpvEventDispatcher(private val coroutineScope: CoroutineScope) : MPVLib.EventObserver {

    private val _events = MutableSharedFlow<MpvEvent>(extraBufferCapacity = 64)
    val events: SharedFlow<MpvEvent> = _events.asSharedFlow()

    fun register() {
        MPVLib.addObserver(this)
    }

    fun unregister() {
        MPVLib.removeObserver(this)
    }

    override fun eventProperty(property: String, value: Boolean) {
        coroutineScope.launch {
            when (property) {
                "pause" -> _events.emit(MpvEvent.Pause(value))
                "eof-reached" -> if (value) _events.emit(MpvEvent.EofReached)
                "paused-for-cache" -> _events.emit(MpvEvent.BufferingChanged(value))
            }
        }
    }

    // Implementing the Double overload for time-pos and duration
    override fun eventProperty(property: String, value: Double) {
        coroutineScope.launch {
            when (property) {
                "time-pos" -> _events.emit(MpvEvent.TimePos(value))
                "duration" -> _events.emit(MpvEvent.Duration(value))
            }
        }
    }

    override fun eventProperty(property: String, value: Long) {
        // Fallback for MPV_FORMAT_DOUBLE if mpv-android delivers it as Long
        coroutineScope.launch {
            when (property) {
                "time-pos" -> _events.emit(MpvEvent.TimePos(value.toDouble()))
                "duration" -> _events.emit(MpvEvent.Duration(value.toDouble()))
                "video-params/w" -> _events.emit(MpvEvent.VideoWidth(value.toInt()))
                "video-params/h" -> _events.emit(MpvEvent.VideoHeight(value.toInt()))
            }
        }
    }

    override fun eventProperty(property: String, value: String) {
        coroutineScope.launch {
            when (property) {
                "track-list" -> _events.emit(MpvEvent.TrackListChanged)
            }
        }
    }

    override fun eventProperty(property: String) {
        // Base implementation for when value is not provided
    }

    override fun eventProperty(property: String, value: `is`.xyz.mpv.MPVNode) {
        // Base implementation
    }

    override fun event(eventId: Int, node: `is`.xyz.mpv.MPVNode) {
        // Handle raw native events here if necessary
    }
}
