package com.example.twdist_android.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.twdist_android.core.ui.navigation.BottomBar
import com.example.twdist_android.core.ui.navigation.BottomBarItem

@Composable
fun AppScaffold(content: @Composable () -> Unit) {
    Scaffold(
        bottomBar = {
            BottomBar(
                items = listOf(
                    BottomBarItem("today", "Today", Icons.Default.DateRange),
                    BottomBarItem("upcoming", "Upcoming", Icons.Default.DateRange),
                    BottomBarItem("favorites", "Favorites", Icons.Default.Star),
                    BottomBarItem("more", "More options", Icons.Default.Menu)
                ),
                onNavigationClick = {}
            )
        }
    ) { innerPadding ->
        Row(Modifier.padding(innerPadding)) {
            content()
        }
    }
}

@Preview
@Composable
fun AppScaffoldPreview() {
    AppScaffold { Text("Preview text (Here is where all the UI should be)") }
}