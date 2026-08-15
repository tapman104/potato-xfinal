package com.yourapp.feature.home

import com.yourapp.feature.player.PlayerActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
class MainActivity : ComponentActivity() {

    private val openVideoLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            val intent = Intent(this, PlayerActivity::class.java).apply {
                data = uri
            }
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(
                onOpenVideoClick = {
                    openVideoLauncher.launch(arrayOf("video/*"))
                }
            )
        }
    }


}
