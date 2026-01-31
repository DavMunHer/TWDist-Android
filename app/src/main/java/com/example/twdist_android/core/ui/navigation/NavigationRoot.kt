package com.example.twdist_android.core.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.twdist_android.core.ui.components.AppScaffold
import com.example.twdist_android.features.auth.presentation.screens.LoginScreen
import com.example.twdist_android.features.auth.presentation.screens.RegisterScreen
import com.example.twdist_android.features.explore.presentation.screens.ExploreScreenPreview
import com.example.twdist_android.features.favorite.presentation.screens.FavoriteProjectScreenPreview
import com.example.twdist_android.features.today.presentation.screens.TodayScreenPreview
import com.example.twdist_android.features.upcoming.presentation.screens.UpcomingScreenPreview
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
data object RegisterScreenKey : AppScreen

@Serializable
data object LoginScreenKey : AppScreen

@Serializable
data object ExplorerScreenKey : AppScreen

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(RegisterScreenKey)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<TodayScreenKey> {
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    TodayScreenPreview()
                }
            }
            entry<UpcomingScreenKey> {
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    UpcomingScreenPreview()
                }
            }
            entry<FavoriteScreenKey> {
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    FavoriteProjectScreenPreview()
                }
            }
            entry<ExplorerScreenKey> {
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    ExploreScreenPreview()
                }
            }
            entry<RegisterScreenKey> {
                Scaffold { padding ->
                    Row(
                        modifier = Modifier
                            .padding(padding)
                    ) {
                        RegisterScreen(
                            onNavigateToLogin = {
                                backStack.add(LoginScreenKey)
                            },
                            onTermsClick = {},
                            onPrivacyClick = {}
                        )
                    }
                }
            }
            entry<LoginScreenKey> {
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    LoginScreen()
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
