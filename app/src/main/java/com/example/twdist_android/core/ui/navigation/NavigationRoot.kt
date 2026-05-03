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
import com.example.twdist_android.features.explore.presentation.screens.ExploreScreen
import com.example.twdist_android.features.favorite.presentation.screens.FavoriteProjectScreen
import com.example.twdist_android.features.projectdetails.presentation.screens.ProjectDetailsScreen
import com.example.twdist_android.features.taskdetails.presentation.screens.TaskDetailsScreen
import com.example.twdist_android.features.today.presentation.screens.TodayScreen
import com.example.twdist_android.features.upcoming.presentation.screens.UpcomingScreen
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

@Serializable
data class ProjectDetailsScreenKey(val projectId: Long) : AppScreen

@Serializable
data class TaskDetailsScreenKey(
    val projectId: Long,
    val sectionId: Long,
    val taskId: Long
) : AppScreen

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(LoginScreenKey)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<TodayScreenKey> {
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    TodayScreen()
                }
            }
            entry<UpcomingScreenKey> {
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    UpcomingScreen()
                }
            }
            entry<FavoriteScreenKey> {
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    FavoriteProjectScreen(
                        onNavigateToProjectDetails = { projectId ->
                            backStack.add(ProjectDetailsScreenKey(projectId))
                        }
                    )
                }
            }
            entry<ExplorerScreenKey> {
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    ExploreScreen(
                        onNavigateToProjectDetails = { projectId ->
                            backStack.add(ProjectDetailsScreenKey(projectId))
                        }
                    )
                }
            }
            entry<ProjectDetailsScreenKey> { key ->
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    ProjectDetailsScreen(
                        projectId = key.projectId,
                        onTaskClicked = { projectId, sectionId, taskId ->
                            backStack.add(
                                TaskDetailsScreenKey(
                                    projectId = projectId,
                                    sectionId = sectionId,
                                    taskId = taskId
                                )
                            )
                        },
                        onProjectDeleted = {
                            val stack = backStack as MutableList<NavKey>
                            if (stack.isNotEmpty()) {
                                stack.removeAt(stack.lastIndex)
                            }
                        }
                    )
                }
            }
            entry<TaskDetailsScreenKey> { key ->
                AppScaffold(onNavItemClick = { (backStack as MutableList<NavKey>).add(it) }) {
                    TaskDetailsScreen(
                        projectId = key.projectId,
                        sectionId = key.sectionId,
                        taskId = key.taskId,
                        onNavigateBackToProjectDetails = {
                            val stack = backStack as MutableList<NavKey>
                            if (stack.isNotEmpty()) {
                                stack.removeAt(stack.lastIndex)
                            }
                        }
                    )
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
                            onTermsClick = {}, //TODO: Add navigation to Terms screen
                            onPrivacyClick = {}
                        )
                    }
                }
            }
            entry<LoginScreenKey> {
                Scaffold { padding ->
                    Row(
                        modifier = Modifier
                            .padding(padding)
                    ) {
                        LoginScreen(
                            onNavigateToRegister = {
                                backStack.add(RegisterScreenKey)
                            },
                            onTermsClick = {}, //TODO: Add navigation to Terms screen
                            onPrivacyClick = {},
                            onNavigateToExplore = {
                                backStack.add(ExplorerScreenKey)
                            }
                        )
                    }
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
