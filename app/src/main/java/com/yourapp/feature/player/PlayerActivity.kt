package com.yourapp.feature.player

import android.os.Bundle
import android.view.SurfaceHolder
import androidx.activity.ComponentActivity
import com.yourapp.R
import com.yourapp.engine.mpv.PlayerSurface
import `is`.xyz.mpv.MPVLib

class PlayerActivity : ComponentActivity() {

    private lateinit var playerSurface: PlayerSurface
    private var isLoaded = false

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
