package com.yourapp.feature.player

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yourapp.R
import com.yourapp.domain.PlayerEngine

class PlayerActivity : ComponentActivity() {

    private lateinit var playerEngine: PlayerEngine
    private lateinit var playerView: android.view.View
    private var isLoaded = false
    private var orientationSet = false

    private val viewModel: PlayerViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return PlayerViewModel(application, playerEngine) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.mpv_surface, null)
        playerView = view
        playerEngine = view as PlayerEngine

        val uri = intent.data
        val fileName = uri?.lastPathSegment ?: "Unknown Video"

        if (!isLoaded) {
            isLoaded = true
            uri?.let {
                val path = getUsablePath(it)
                if (path != null) {
                    playerView.javaClass.getMethod("playFile", String::class.java).invoke(playerView, path)
                }
            }
        }

        playerView.javaClass.getMethod("initialize", String::class.java, String::class.java)
            .invoke(playerView, applicationContext.filesDir.path, applicationContext.cacheDir.path)

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            
            if (!orientationSet && uiState.videoWidth > 0 && uiState.videoHeight > 0) {
                orientationSet = true
                requestedOrientation = if (uiState.videoWidth > uiState.videoHeight)
                    android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                else
                    android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            }

            PlayerScreen(
                uiState = uiState,
                fileName = fileName,
                onBack = { finish() },
                onPlayPause = { viewModel.togglePlayPause() },
                onSeek = { viewModel.seekTo(it) },
                videoSurface = {
                    AndroidView(
                        factory = { playerView }
                    )
                }
            )
        }
    }

    private fun getUsablePath(uri: android.net.Uri): String? {
        if (uri.scheme == "file") return uri.path
        
        try {
            val pfd = contentResolver.openFileDescriptor(uri, "r")
            if (pfd != null) {
                val fd = pfd.detachFd()
                return "fdclose://$fd"
            }
        } catch (e: Exception) {}
        
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            playerView.javaClass.getMethod("destroy").invoke(playerView)
        } catch (e: Exception) {}
    }
}
