package com.yourapp.feature.player

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourapp.domain.PlaybackState
import com.yourapp.domain.PlayerUiState
import com.yourapp.ui.player.PlayerControlsBottomRow
import com.yourapp.ui.player.PlayerPlayPauseButton
import com.yourapp.ui.player.PlayerSeekBar
import com.yourapp.ui.player.PlayerTopBar
import kotlinx.coroutines.launch

@Composable
fun PlayerScreen(
    uiState: PlayerUiState,
    fileName: String,
    onBack: () -> Unit,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier,
    videoSurface: @Composable () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val onComingSoon = {
        coroutineScope.launch {
            snackbarHostState.showSnackbar("Coming soon")
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Video surface fills the entire box
        videoSurface()

        // Controls overlay
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            PlayerTopBar(
                fileName = fileName,
                onBack = onBack,
                onComingSoon = { onComingSoon() }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            PlayerSeekBar(
                positionMs = uiState.positionMs,
                durationMs = uiState.durationMs,
                onSeek = onSeek
            )
            
            PlayerControlsBottomRow(
                onComingSoon = { onComingSoon() },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        // Center play/pause
        PlayerPlayPauseButton(
            playbackState = uiState.playbackState,
            onPlayPause = onPlayPause,
            modifier = Modifier.align(Alignment.Center)
        )

        // Snackbar host for "Coming soon"
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PlayerScreenPreview() {
    MaterialTheme {
        PlayerScreen(
            uiState = PlayerUiState(
                playbackState = PlaybackState.Playing,
                positionMs = 2025000L,
                durationMs = 3096000L
            ),
            fileName = "Our.Sticky.Love.S01E01.1080p.mkv",
            onBack = {},
            onPlayPause = {},
            onSeek = {}
        )
    }
}
