package com.example.twdist_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.twdist_android.core.ui.navigation.NavigationRoot
import com.example.twdist_android.core.ui.theme.TWDistAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TWDistAndroidTheme {
                NavigationRoot()
            }
        }
    }
}
