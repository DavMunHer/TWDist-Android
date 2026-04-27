package com.example.twdist_android.features.taskdetails.presentation.model

sealed interface TaskDetailsUiEvent {
    data object NavigateBackToProjectDetails : TaskDetailsUiEvent
}
