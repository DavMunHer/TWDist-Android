package com.example.twdist_android.features.explore.presentation.components.recyclerview

import android.graphics.Color

data class ProjectRowColors(
    val surfaceVariant: Int,
    val primary: Int,
    val onSurface: Int
) {
    companion object {
        fun defaultLightFallback(): ProjectRowColors = ProjectRowColors(
            surfaceVariant = Color.parseColor("#E7E0EC"),
            primary = Color.parseColor("#6650a4"),
            onSurface = Color.parseColor("#1C1B1F")
        )
    }
}
