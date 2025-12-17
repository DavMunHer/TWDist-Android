package com.example.twdist_android.core.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.twdist_android.core.ui.components.AppScaffold
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppScreen : NavKey

@Serializable
data object TodayScreenKey : AppScreen

@Serializable
data object UpcomingScreenKey : AppScreen

@Serializable
data object FavoriteScreenKey : AppScreen

@Serializable
data object MoreScreenKey : AppScreen

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(TodayScreenKey)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<TodayScreenKey> {
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    Text("This should be replaced by the TodayScreen feature")
                }
            }
            entry<UpcomingScreenKey> {
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    Text("This should be replaced by the Upcoming Screen feature")
                }
            }
            entry<FavoriteScreenKey> {
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    Text("This should be replaced by the Favorite Screen feature")
                }
            }
            entry<MoreScreenKey> {
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    Text("This should be replaced by the More Screen feature")
                }
            }
        }
    )
}

@Preview
@Composable
fun NavigationRootPreview() {
    NavigationRoot()
}
