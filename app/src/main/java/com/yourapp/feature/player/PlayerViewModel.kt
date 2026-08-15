package com.yourapp.feature.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yourapp.domain.PlaybackState
import com.yourapp.domain.PlayerUiState
import com.yourapp.engine.mpv.MpvEvent
import com.yourapp.engine.mpv.MpvEventDispatcher
import `is`.xyz.mpv.MPVLib
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val mpvEventDispatcher = MpvEventDispatcher(viewModelScope)
    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    init {
        mpvEventDispatcher.register()
        viewModelScope.launch {
            mpvEventDispatcher.events.collect { event ->
                handleMpvEvent(event)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mpvEventDispatcher.unregister()
    }

    private fun handleMpvEvent(event: MpvEvent) {
        when (event) {
            is MpvEvent.Pause -> {
                _uiState.update { 
                    it.copy(playbackState = if (event.paused) PlaybackState.Paused else PlaybackState.Playing) 
                }
            }
            is MpvEvent.TimePos -> {
                _uiState.update { it.copy(positionMs = (event.seconds * 1000).toLong()) }
            }
            is MpvEvent.Duration -> {
                _uiState.update { it.copy(durationMs = (event.seconds * 1000).toLong()) }
            }
            is MpvEvent.EofReached -> {
                _uiState.update { it.copy(playbackState = PlaybackState.Ended) }
            }
            is MpvEvent.BufferingChanged -> { }
            is MpvEvent.TrackListChanged -> { }
        }
    }

    fun togglePlayPause() {
        MPVLib.command("cycle", "pause")
    }

    fun seekTo(positionMs: Long) {
        val seconds = positionMs / 1000.0
        MPVLib.command("seek", seconds.toString(), "absolute")
    }

    fun setAudioTrack(id: Int) {
        MPVLib.setPropertyInt("aid", id)
    }

    fun setSubtitleTrack(id: Int) {
        MPVLib.setPropertyInt("sid", id)
    }

    fun setSpeed(speed: Float) {
        MPVLib.setPropertyDouble("speed", speed.toDouble())
        _uiState.update { it.copy(playbackSpeed = speed) }
    }
}
