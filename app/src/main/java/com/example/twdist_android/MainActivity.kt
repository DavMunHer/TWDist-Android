package com.example.twdist_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.example.twdist_android.core.ui.navigation.NavigationRoot
import com.example.twdist_android.core.ui.theme.TWDistAndroidTheme
import com.example.twdist_android.ui.MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TWDistAndroidTheme {
                NavigationRoot()
            }
        }

        // Example usage to ensure injection works
        val greeting = viewModel.greeting()
        println(greeting)
    }
}
