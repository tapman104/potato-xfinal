package com.yourapp.feature.player

import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yourapp.R
import com.yourapp.engine.mpv.PlayerSurface
import `is`.xyz.mpv.MPVLib
import kotlinx.coroutines.launch

class PlayerActivity : ComponentActivity() {

    private lateinit var playerSurface: PlayerSurface
    private var isLoaded = false
    private val viewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val view = layoutInflater.inflate(R.layout.mpv_surface, null)
        playerSurface = view as PlayerSurface
        setContentView(playerSurface)

        if (!isLoaded) {
            isLoaded = true
            intent.data?.let { uri ->
                val path = getUsablePath(uri)
                if (path != null) {
                    playerSurface.playFile(path)
                }
            }
        }

        playerSurface.initialize(applicationContext.filesDir.path, applicationContext.cacheDir.path)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    Log.d("PlayerActivity", "uiState: $state")
                }
            }
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
        playerSurface.destroy()
    }
}
