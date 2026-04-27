package com.example.twdist_android.features.projectdetails.presentation.model

data class ProjectDetailsUiState(
    val isLoading: Boolean = true,
    val project: ProjectDetailsUi? = null,
    val error: String? = null,
    val openProjectMenu: Boolean = false,
    val isEditingProject: Boolean = false,
    val editingProjectName: String = "",
    val projectActionError: String? = null,
    val deleteConfirmProject: Boolean = false,
    val projectDeleted: Boolean = false,
    val openSectionMenuForId: Long? = null,
    val editingSectionId: Long? = null,
    val editingSectionName: String = "",
    val isCreatingSection: Boolean = false,
    val creatingSectionName: String = "",
    val isSectionCreateLoading: Boolean = false,
    val sectionActionError: String? = null,
    val taskActionError: String? = null,
    val isTaskCreateLoading: Boolean = false,
    val isTaskEditLoading: Boolean = false,
    val isTaskDeleteLoading: Boolean = false,
    val deleteConfirmSectionId: Long? = null,
    val creatingTaskSectionId: Long? = null,
    val creatingTaskName: String = "",
    val editingTaskSectionId: Long? = null,
    val editingTaskId: Long? = null,
    val editingTaskName: String = "",
    val deleteConfirmTaskSectionId: Long? = null,
    val deleteConfirmTaskId: Long? = null,
    val openTaskMenuForId: Long? = null,
    val taskCompletionUndo: TaskCompletionUndo? = null,
    val taskSnackbarMessage: String? = null,
    val sectionItems: List<SectionUi> = emptyList(),
    val tasksById: Map<String, TaskUi> = emptyMap()
)

data class ProjectDetailsUi(
    val id: Long,
    val name: String,
    val isFavorite: Boolean,
    val sections: List<String>,
)

data class SectionUi(
    val id: Long,
    val name: String,
    val taskIds: List<String>
)

data class TaskUi(
    val id: Long,
    val name: String,
    val completed: Boolean,
    val description: String? = null,
    val startDate: String? = null,
    val endDate: String? = null
)

data class TaskCompletionUndo(
    val sectionId: Long,
    val task: TaskUi
)
