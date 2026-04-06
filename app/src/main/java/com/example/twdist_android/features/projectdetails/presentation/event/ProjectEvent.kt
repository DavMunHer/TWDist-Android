package com.example.twdist_android.features.projectdetails.presentation.event

sealed interface ProjectEvent {
    data object MenuOpened : ProjectEvent
    data object MenuDismissed : ProjectEvent

    data object EditClicked : ProjectEvent
    data class NameChanged(val name: String) : ProjectEvent
    data object EditConfirmed : ProjectEvent
    data object EditDismissed : ProjectEvent

    data object DeleteClicked : ProjectEvent
    data object DeleteConfirmed : ProjectEvent
    data object DeleteDismissed : ProjectEvent
    data object DeletedHandled : ProjectEvent
}

