package com.example.twdist_android.features.projectdetails.presentation.event

sealed interface TaskEvent {
    data class AddTaskClicked(val sectionId: Long) : TaskEvent
    data class CreateTaskNameChanged(val name: String) : TaskEvent
    data object CreateTaskConfirmed : TaskEvent
    data object CreateTaskDismissed : TaskEvent
    data class TaskMenuOpened(val taskId: Long) : TaskEvent
    data object TaskMenuDismissed : TaskEvent
    data class EditTaskClicked(val sectionId: Long, val taskId: Long) : TaskEvent
    data class EditTaskNameChanged(val name: String) : TaskEvent
    data object EditTaskConfirmed : TaskEvent
    data object EditTaskDismissed : TaskEvent
    data class DeleteTaskClicked(val sectionId: Long, val taskId: Long) : TaskEvent
    data object DeleteTaskConfirmed : TaskEvent
    data object DeleteTaskDismissed : TaskEvent
    data class TaskCompletionToggled(val sectionId: Long, val taskId: Long) : TaskEvent
}
