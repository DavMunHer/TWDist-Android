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
data object TodayScreenKey : NavKey

@Serializable
data object UpcomingScreenKey : NavKey

@Serializable
data object FavoriteScreenKey : NavKey

@Serializable
data object MoreScreenKey : NavKey

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(TodayScreenKey)

    NavDisplay(
        backStack,
        entryProvider = entryProvider {
            entry<TodayScreenKey> {
                AppScaffold(onNavItemClick = { backStack.add(it) }) {
                    Text("This should be replaced by the TodayScreen feature")
                }
            }
            entry<UpcomingScreenKey> {
                AppScaffold(onNavItemClick = { backStack.add(it) }) {
                    Text("This should be replaced by the Upcoming Screen feature")
                }
            }
            entry<FavoriteScreenKey> {
                AppScaffold(onNavItemClick = { backStack.add(it) }) {
                    Text("This should be replaced by the Favorite Screen feature")
                }
            }
            entry<MoreScreenKey> {
                AppScaffold(onNavItemClick = { backStack.add(it) }) {
                    Text("This should be replaced by the More Screen feature")
                }
            }
        }
    )
}

@Preview
@Composable
fun NavigationRootPreview() {
    //FIXME: This isn't working just yet
    NavigationRoot()
}
