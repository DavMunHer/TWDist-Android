package com.example.twdist_android.features.projectdetails.presentation.event

sealed interface SectionEvent {
    data object AddSectionClicked : SectionEvent
    data class CreateSectionNameChanged(val name: String) : SectionEvent
    data object CreateSectionConfirmed : SectionEvent
    data object CreateSectionDismissed : SectionEvent
    data class MenuOpened(val sectionId: Long) : SectionEvent
    data object MenuDismissed : SectionEvent
    data class EditClicked(val sectionId: Long) : SectionEvent
    data class NameChanged(val name: String) : SectionEvent
    data object EditConfirmed : SectionEvent
    data object EditDismissed : SectionEvent
    data class DeleteClicked(val sectionId: Long) : SectionEvent
    data object DeleteConfirmed : SectionEvent
    data object DeleteDismissed : SectionEvent
}
