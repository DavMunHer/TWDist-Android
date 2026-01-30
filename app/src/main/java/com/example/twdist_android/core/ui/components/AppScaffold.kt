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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import com.example.twdist_android.core.ui.navigation.BottomBar
import com.example.twdist_android.core.ui.navigation.BottomBarItem
import com.example.twdist_android.core.ui.navigation.ExplorerScreenKey
import com.example.twdist_android.core.ui.navigation.FavoriteScreenKey
import com.example.twdist_android.core.ui.navigation.TodayScreenKey
import com.example.twdist_android.core.ui.navigation.UpcomingScreenKey

@Composable
fun AppScaffold(onNavItemClick: (NavKey) -> Unit, content: @Composable () -> Unit) {
    Scaffold(
        bottomBar = {
            BottomBar(
                items = listOf(
                    BottomBarItem(key = TodayScreenKey, "Today", Icons.Default.DateRange),
                    BottomBarItem(key = UpcomingScreenKey, "Upcoming", Icons.Default.DateRange),
                    BottomBarItem(key = FavoriteScreenKey, "Favorites", Icons.Default.Star),
                    BottomBarItem(key = ExplorerScreenKey, "Explore", Icons.Default.Menu)
                ),
                onNavigationClick = onNavItemClick
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
    var debugging = remember { mutableStateOf<NavKey>(TodayScreenKey) }
    AppScaffold(onNavItemClick = {
        debugging.value = it
    }) {
        Text("Preview text (Here is where all the UI should be)")
        Text("${debugging.value}")
    }
}