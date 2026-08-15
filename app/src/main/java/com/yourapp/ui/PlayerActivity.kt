package com.yourapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.yourapp.R
import com.yourapp.engine.MpvController
import com.yourapp.engine.MpvSurfaceManager
import `is`.xyz.mpv.MPVLib

class PlayerActivity : ComponentActivity() {

    private val mpvController = MpvController()
    private lateinit var mpvSurfaceManager: MpvSurfaceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MPVLib.create(applicationContext)
        MPVLib.init()

        val view = layoutInflater.inflate(R.layout.mpv_surface, null)
        mpvSurfaceManager = view as MpvSurfaceManager
        setContentView(mpvSurfaceManager)

        mpvSurfaceManager.initialize(applicationContext.filesDir.path)

        intent.data?.let { uri ->
            mpvController.loadFile(uri.toString())
            mpvController.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mpvController.stop()
        MPVLib.destroy()
    }
}
