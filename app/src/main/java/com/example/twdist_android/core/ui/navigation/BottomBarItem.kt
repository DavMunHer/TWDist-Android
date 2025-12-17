package com.example.twdist_android.core.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey


data class BottomBarItem(
    val key: NavKey,
    val label: String,
    val icon: ImageVector
)