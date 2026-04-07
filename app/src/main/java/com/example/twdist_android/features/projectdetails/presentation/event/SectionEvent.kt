package com.example.twdist_android.features.projectdetails.presentation.event

sealed interface SectionEvent {
    data class MenuOpened(val sectionId: Long) : SectionEvent
    data object MenuDismissed : SectionEvent
    data class EditClicked(val sectionId: Long) : SectionEvent
    data class NameChanged(val name: String) : SectionEvent
    data object EditConfirmed : SectionEvent
    data object EditDismissed : SectionEvent
    data class DeleteClicked(val sectionId: Long) : SectionEvent
    data object DeleteConfirmed : SectionEvent
    data object DeleteDismissed : SectionEvent
    data class AddTaskClicked(val sectionId: Long) : SectionEvent
    data class CreateTaskNameChanged(val name: String) : SectionEvent
    data object CreateTaskConfirmed : SectionEvent
    data object CreateTaskDismissed : SectionEvent
    data class TaskMenuOpened(val taskId: Long) : SectionEvent
    data object TaskMenuDismissed : SectionEvent
    data class EditTaskClicked(val sectionId: Long, val taskId: Long) : SectionEvent
    data class EditTaskNameChanged(val name: String) : SectionEvent
    data object EditTaskConfirmed : SectionEvent
    data object EditTaskDismissed : SectionEvent
    data class DeleteTaskClicked(val sectionId: Long, val taskId: Long) : SectionEvent
    data object DeleteTaskConfirmed : SectionEvent
    data object DeleteTaskDismissed : SectionEvent
    data class TaskCompletionToggled(val sectionId: Long, val taskId: Long) : SectionEvent
}
