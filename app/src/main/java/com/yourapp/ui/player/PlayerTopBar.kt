package com.yourapp.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlayerTopBar(
    fileName: String,
    onBack: () -> Unit,
    onComingSoon: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor = Color(0xFF1A1A1A).copy(alpha = 0.7f)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.background(buttonColor, CircleShape)
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = fileName,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(
            onClick = onComingSoon,
            modifier = Modifier.background(buttonColor, CircleShape)
        ) {
            Icon(imageVector = Icons.Filled.Audiotrack, contentDescription = "Audio Track", tint = Color.White)
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onComingSoon,
            modifier = Modifier.background(buttonColor, CircleShape)
        ) {
            Icon(imageVector = Icons.Filled.Subtitles, contentDescription = "Subtitles", tint = Color.White)
        }

        Spacer(modifier = Modifier.width(8.dp))

        TextButton(
            onClick = onComingSoon,
            modifier = Modifier.background(buttonColor, CircleShape),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = "HW", color = Color.White)
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onComingSoon,
            modifier = Modifier.background(buttonColor, CircleShape)
        ) {
            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More", tint = Color.White)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PlayerTopBarPreview() {
    PlayerTopBar(
        fileName = "Our.Sticky.Love.S01E01.1080p.mkv",
        onBack = {},
        onComingSoon = {}
    )
}
