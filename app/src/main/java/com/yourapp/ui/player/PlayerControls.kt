package com.yourapp.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PictureInPicture
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourapp.domain.PlaybackState

@Composable
fun PlayerControlsBottomRow(
    onComingSoon: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor = Color(0xFF1A1A1A).copy(alpha = 0.7f)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Since material icons might be missing extended ones out of the box, 
        // we'll use base icons that are close enough or standard ones.
        // Or if standard ones are guaranteed available:
        
        // 1. Previous (skip to start)
        IconButton(onClick = onComingSoon, modifier = Modifier.background(buttonColor, CircleShape)) {
            Icon(Icons.Filled.SkipPrevious, contentDescription = "Previous", tint = Color.White)
        }
        // 2. Lock
        IconButton(onClick = onComingSoon, modifier = Modifier.background(buttonColor, CircleShape)) {
            Icon(Icons.Filled.Lock, contentDescription = "Lock", tint = Color.White)
        }
        // 3. Next (skip to end)
        IconButton(onClick = onComingSoon, modifier = Modifier.background(buttonColor, CircleShape)) {
            Icon(Icons.Filled.SkipNext, contentDescription = "Next", tint = Color.White)
        }
        // 4. Fit/aspect ratio
        IconButton(onClick = onComingSoon, modifier = Modifier.background(buttonColor, CircleShape)) {
            Icon(Icons.Filled.AspectRatio, contentDescription = "Aspect Ratio", tint = Color.White)
        }
        // 5. Rotate/orientation
        IconButton(onClick = onComingSoon, modifier = Modifier.background(buttonColor, CircleShape)) {
            Icon(Icons.Filled.ScreenRotation, contentDescription = "Rotate", tint = Color.White)
        }
        // 6. PiP
        IconButton(onClick = onComingSoon, modifier = Modifier.background(buttonColor, CircleShape)) {
            Icon(Icons.Filled.PictureInPicture, contentDescription = "PiP", tint = Color.White)
        }
    }
}

@Composable
fun PlayerPlayPauseButton(
    playbackState: PlaybackState,
    onPlayPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor = Color(0xFF1A1A1A).copy(alpha = 0.7f)
    val isPlaying = playbackState == PlaybackState.Playing
    IconButton(
        onClick = onPlayPause,
        modifier = modifier
            .size(80.dp)
            .background(buttonColor, CircleShape)
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Play",
            tint = Color.White,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PlayerControlsBottomRowPreview() {
    PlayerControlsBottomRow(onComingSoon = {})
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PlayerPlayPauseButtonPreview() {
    PlayerPlayPauseButton(playbackState = PlaybackState.Playing, onPlayPause = {})
}
