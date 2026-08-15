package com.yourapp.domain

import kotlinx.coroutines.flow.Flow

interface PlayerEngine {
    val events: Flow<PlayerEvent>
    fun play()
    fun pause()
    fun seekTo(positionMs: Long)
    fun setAudioTrack(id: Int)
    fun setSubtitleTrack(id: Int)
    fun setSpeed(speed: Float)
    fun setDecoder(mode: DecoderMode)
}
