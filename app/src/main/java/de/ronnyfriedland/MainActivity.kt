package de.ronnyfriedland

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import de.ronnyfriedland.ui.FitProgressApp

/** Main entry point of the application. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitProgressApp(this)
        }
    }
}
