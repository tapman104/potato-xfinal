package com.yourapp.domain

data class PlayerUiState(
    val playbackState: PlaybackState = PlaybackState.Idle,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val audioTracks: List<TrackUiModel> = emptyList(),
    val subtitleTracks: List<TrackUiModel> = emptyList(),
    val playbackSpeed: Float = 1.0f
)
