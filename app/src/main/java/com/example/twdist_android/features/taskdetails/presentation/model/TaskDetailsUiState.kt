package com.example.twdist_android.features.taskdetails.presentation.model

import com.example.twdist_android.features.projectdetails.presentation.model.TaskUi

data class TaskDetailsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val projectId: Long? = null,
    val projectName: String = "",
    val sectionId: Long? = null,
    val taskId: Long? = null,
    val task: TaskUi? = null,
    val openProjectMenu: Boolean = false,
    val openTaskMenuForId: Long? = null,
    val editingTaskId: Long? = null,
    val editingTaskName: String = "",
    val deleteConfirmTaskId: Long? = null,
    val taskActionError: String? = null,
    val isTaskEditLoading: Boolean = false,
    val isTaskDeleteLoading: Boolean = false,
    val startDate: String = "",
    val endDate: String = "",
    val description: String = "",
    val isSaving: Boolean = false,
    val saveMessage: String? = null
)
