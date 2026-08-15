package com.yourapp.ui.player

import android.text.format.DateUtils
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlayerSeekBar(
    positionMs: Long,
    durationMs: Long,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = DateUtils.formatElapsedTime(positionMs / 1000),
            color = Color.White
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Slider(
            value = if (durationMs > 0) (positionMs.toFloat() / durationMs.toFloat()) else 0f,
            onValueChange = { percent ->
                onSeek((percent * durationMs).toLong())
            },
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
            )
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = DateUtils.formatElapsedTime(durationMs / 1000),
            color = Color.White
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PlayerSeekBarPreview() {
    PlayerSeekBar(
        positionMs = 2025000L,
        durationMs = 3096000L,
        onSeek = {}
    )
}
