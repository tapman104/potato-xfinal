package com.yourapp.domain

sealed class PlayerEvent {
    data class PositionChanged(val positionMs: Long) : PlayerEvent()
    data class DurationChanged(val durationMs: Long) : PlayerEvent()
    data class PlaybackStateChanged(val state: PlaybackState) : PlayerEvent()
    data class TracksChanged(
        val audioTracks: List<TrackUiModel>,
        val subtitleTracks: List<TrackUiModel>
    ) : PlayerEvent()
    data class VideoSizeChanged(val width: Int, val height: Int) : PlayerEvent()
}
