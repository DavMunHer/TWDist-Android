package com.example.twdist_android.core.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey

@Composable
fun BottomBar(
    items: List<BottomBarItem>,
    onNavigationClick: (key: NavKey) -> Unit
) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = false,
                onClick = { onNavigationClick(item.key) },
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.label) }
            )
        }
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    val items = listOf<BottomBarItem>(
        BottomBarItem(TodayScreenKey, "Today", Icons.Default.DateRange),
        BottomBarItem(UpcomingScreenKey, "Upcoming", Icons.Default.DateRange),
        BottomBarItem(FavoriteScreenKey, "Favorites", Icons.Default.Star),
        BottomBarItem(MoreScreenKey, "More options", Icons.Default.Menu)
    )

    BottomBar(items, onNavigationClick = {})
}