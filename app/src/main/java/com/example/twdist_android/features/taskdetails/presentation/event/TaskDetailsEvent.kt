package com.example.twdist_android.features.taskdetails.presentation.event

sealed interface TaskDetailsEvent {
    data object MenuOpened : TaskDetailsEvent
    data object MenuDismissed : TaskDetailsEvent
    data object MenuEditClicked : TaskDetailsEvent
    data object MenuDeleteClicked : TaskDetailsEvent
    data object TaskMenuDismissed : TaskDetailsEvent
    data object TaskEditDismissed : TaskDetailsEvent
    data class TaskMenuOpened(val taskId: Long) : TaskDetailsEvent
    data class TaskCompletionToggled(val sectionId: Long, val taskId: Long) : TaskDetailsEvent
    data class TaskEditClicked(val sectionId: Long, val taskId: Long) : TaskDetailsEvent
    data class TaskEditNameChanged(val value: String) : TaskDetailsEvent
    data object TaskEditConfirmed : TaskDetailsEvent
    data class TaskDeleteClicked(val sectionId: Long, val taskId: Long) : TaskDetailsEvent
    data object TaskDeleteDismissed : TaskDetailsEvent
    data object TaskDeleteConfirmed : TaskDetailsEvent
    data class StartDateChanged(val value: String) : TaskDetailsEvent
    data class EndDateChanged(val value: String) : TaskDetailsEvent
    data class DescriptionChanged(val value: String) : TaskDetailsEvent
    data object SaveClicked : TaskDetailsEvent
    data object SaveMessageShown : TaskDetailsEvent
}
