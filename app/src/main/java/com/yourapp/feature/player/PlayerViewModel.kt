package com.yourapp.feature.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yourapp.domain.PlaybackState
import com.yourapp.domain.PlayerEngine
import com.yourapp.domain.PlayerEvent
import com.yourapp.domain.PlayerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerViewModel(
    application: Application,
    private val playerEngine: PlayerEngine
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            playerEngine.events.collect { event ->
                handlePlayerEvent(event)
            }
        }
    }

    private fun handlePlayerEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.PlaybackStateChanged -> {
                _uiState.update { it.copy(playbackState = event.state) }
            }
            is PlayerEvent.PositionChanged -> {
                _uiState.update { it.copy(positionMs = event.positionMs) }
            }
            is PlayerEvent.DurationChanged -> {
                _uiState.update { it.copy(durationMs = event.durationMs) }
            }
            is PlayerEvent.TracksChanged -> {
                _uiState.update { 
                    it.copy(audioTracks = event.audioTracks, subtitleTracks = event.subtitleTracks) 
                }
            }
            is PlayerEvent.VideoSizeChanged -> {
                _uiState.update { 
                    it.copy(videoWidth = event.width, videoHeight = event.height) 
                }
            }
        }
    }

    fun togglePlayPause() {
        if (_uiState.value.playbackState == PlaybackState.Playing) {
            playerEngine.pause()
        } else {
            playerEngine.play()
        }
    }

    fun seekTo(positionMs: Long) {
        playerEngine.seekTo(positionMs)
    }

    fun setAudioTrack(id: Int) {
        playerEngine.setAudioTrack(id)
    }

    fun setSubtitleTrack(id: Int) {
        playerEngine.setSubtitleTrack(id)
    }

    fun setSpeed(speed: Float) {
        playerEngine.setSpeed(speed)
        _uiState.update { it.copy(playbackSpeed = speed) }
    }
}
