package com.example.twdist_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.twdist_android.core.ui.components.AppScaffold
import com.example.twdist_android.core.ui.theme.TWDistAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TWDistAndroidTheme {
                AppScaffold(onNavItemClick = {println(it)}) {
                    // This should be replaced by the AppNav composable
                    Text("Route changing UI")
                }
            }
        }
    }
}
