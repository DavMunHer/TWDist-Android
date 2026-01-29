package com.example.twdist_android.features.explore.presentation.event

sealed class ExploreEvent {
    data object ToggleExpanded : ExploreEvent()
    data class CreateProject(val name: String) : ExploreEvent()
    data object LoadProjects : ExploreEvent()
}