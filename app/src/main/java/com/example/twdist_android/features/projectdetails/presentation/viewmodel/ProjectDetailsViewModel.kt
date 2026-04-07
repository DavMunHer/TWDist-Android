package com.example.twdist_android.features.projectdetails.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.features.projectdetails.application.usecases.DeleteProjectUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.DeleteSectionUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.DeleteTaskUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.CreateTaskUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.GetProjectByIdUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.GetTasksBySectionUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.UpdateProjectNameUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.UpdateSectionNameUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.UpdateTaskUseCase
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.domain.model.TaskName
import com.example.twdist_android.features.projectdetails.presentation.event.ProjectEvent
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent
import com.example.twdist_android.features.projectdetails.presentation.mapper.toDetailsUi
import com.example.twdist_android.features.projectdetails.presentation.model.SectionUi
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState
import com.example.twdist_android.features.projectdetails.presentation.model.TaskUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val updateProjectNameUseCase: UpdateProjectNameUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val updateSectionNameUseCase: UpdateSectionNameUseCase,
    private val deleteSectionUseCase: DeleteSectionUseCase,
    private val getTasksBySectionUseCase: GetTasksBySectionUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private var projectId: Long = -1L

    private val _uiState = MutableStateFlow(ProjectDetailsUiState())
    val uiState: StateFlow<ProjectDetailsUiState> = _uiState

    fun loadProjectDetails(projectId: Long) {
        this.projectId = projectId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getProjectByIdUseCase(projectId)
                .onSuccess { aggregate ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            project = aggregate.toDetailsUi(),
                            sectionActionError = null,
                            taskActionError = null,
                            sectionItems = aggregate.sections.map { section ->
                                SectionUi(
                                    id = section.id,
                                    name = section.name.asString(),
                                    taskIds = section.taskIds
                                )
                            },
                            tasksById = emptyMap()
                        )
                    }
                    loadTasksForSections()
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message ?: "Unknown error")
                    }
                }
        }
    }

    fun retry() {
        loadProjectDetails(projectId)
    }

    fun onEvent(event: SectionEvent) {
        when (event) {
            is SectionEvent.MenuOpened -> onSectionOptionsClick(event.sectionId)
            SectionEvent.MenuDismissed -> onSectionOptionsDismiss()
            is SectionEvent.EditClicked -> onEditSectionClick(event.sectionId)
            is SectionEvent.NameChanged -> onEditSectionNameChange(event.name)
            SectionEvent.EditConfirmed -> onSaveSectionEdit()
            SectionEvent.EditDismissed -> onEditSectionDismiss()
            is SectionEvent.DeleteClicked -> onDeleteSectionClick(event.sectionId)
            SectionEvent.DeleteConfirmed -> onDeleteSectionConfirm()
            SectionEvent.DeleteDismissed -> onDeleteSectionDismiss()
            is SectionEvent.AddTaskClicked -> onAddTaskClick(event.sectionId)
            is SectionEvent.CreateTaskNameChanged -> onCreateTaskNameChanged(event.name)
            SectionEvent.CreateTaskConfirmed -> onCreateTaskConfirmed()
            SectionEvent.CreateTaskDismissed -> onCreateTaskDismissed()
            is SectionEvent.TaskMenuOpened -> onTaskMenuOpened(event.taskId)
            SectionEvent.TaskMenuDismissed -> onTaskMenuDismissed()
            is SectionEvent.EditTaskClicked -> onEditTaskClick(event.sectionId, event.taskId)
            is SectionEvent.EditTaskNameChanged -> onEditTaskNameChanged(event.name)
            SectionEvent.EditTaskConfirmed -> onEditTaskConfirmed()
            SectionEvent.EditTaskDismissed -> onEditTaskDismissed()
            is SectionEvent.DeleteTaskClicked -> onDeleteTaskClick(event.sectionId, event.taskId)
            SectionEvent.DeleteTaskConfirmed -> onDeleteTaskConfirmed()
            SectionEvent.DeleteTaskDismissed -> onDeleteTaskDismissed()
            is SectionEvent.TaskCompletionToggled -> onTaskCompletionToggled(event.sectionId, event.taskId)
        }
    }

    fun onProjectEvent(event: ProjectEvent) {
        when (event) {
            ProjectEvent.MenuOpened -> onProjectMenuOpened()
            ProjectEvent.MenuDismissed -> onProjectMenuDismissed()
            ProjectEvent.EditClicked -> onEditProjectClicked()
            is ProjectEvent.NameChanged -> onEditProjectNameChanged(event.name)
            ProjectEvent.EditConfirmed -> onEditProjectConfirmed()
            ProjectEvent.EditDismissed -> onEditProjectDismissed()
            ProjectEvent.DeleteClicked -> onDeleteProjectClicked()
            ProjectEvent.DeleteConfirmed -> onDeleteProjectConfirmed()
            ProjectEvent.DeleteDismissed -> onDeleteProjectDismissed()
            ProjectEvent.DeletedHandled -> onProjectDeletedHandled()
        }
    }

    private fun onProjectMenuOpened() {
        _uiState.update { it.copy(openProjectMenu = true, projectActionError = null) }
    }

    private fun onProjectMenuDismissed() {
        _uiState.update { it.copy(openProjectMenu = false) }
    }

    private fun onEditProjectClicked() {
        val currentName = _uiState.value.project?.name ?: return
        _uiState.update {
            it.copy(
                openProjectMenu = false,
                isEditingProject = true,
                editingProjectName = currentName,
                projectActionError = null
            )
        }
    }

    private fun onEditProjectNameChanged(value: String) {
        _uiState.update { it.copy(editingProjectName = value, projectActionError = null) }
    }

    private fun onEditProjectDismissed() {
        _uiState.update {
            it.copy(
                isEditingProject = false,
                editingProjectName = "",
                projectActionError = null
            )
        }
    }

    private fun onEditProjectConfirmed() {
        val projectId = _uiState.value.project?.id ?: return
        val name = ProjectName.create(_uiState.value.editingProjectName)
            .getOrElse { throwable ->
                _uiState.update { it.copy(projectActionError = throwable.toProjectValidationMessage()) }
                return
            }

        val previousProject = _uiState.value.project
        _uiState.update { state ->
            state.copy(
                project = state.project?.copy(name = name.asString()),
                isEditingProject = false,
                editingProjectName = "",
                projectActionError = null
            )
        }

        viewModelScope.launch {
            updateProjectNameUseCase(projectId, name)
                .onSuccess {
                    _uiState.update { it.copy(projectActionError = null) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            project = previousProject,
                            projectActionError = error.toProjectActionMessage("Could not update project")
                        )
                    }
                }
        }
    }

    private fun onDeleteProjectClicked() {
        _uiState.update {
            it.copy(
                openProjectMenu = false,
                deleteConfirmProject = true,
                projectActionError = null
            )
        }
    }

    private fun onDeleteProjectDismissed() {
        _uiState.update { it.copy(deleteConfirmProject = false) }
    }

    private fun onDeleteProjectConfirmed() {
        val projectId = _uiState.value.project?.id ?: return
        _uiState.update { it.copy(deleteConfirmProject = false, projectActionError = null) }

        viewModelScope.launch {
            deleteProjectUseCase(projectId)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            project = null,
                            projectDeleted = true,
                            projectActionError = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            projectActionError = error.toProjectActionMessage("Could not delete project")
                        )
                    }
                }
        }
    }

    private fun onProjectDeletedHandled() {
        _uiState.update { it.copy(projectDeleted = false) }
    }

    private fun onSectionOptionsClick(sectionId: Long) {
        _uiState.update {
            it.copy(
                openSectionMenuForId = sectionId,
                sectionActionError = null,
                taskActionError = null
            )
        }
    }

    private fun onSectionOptionsDismiss() {
        _uiState.update { it.copy(openSectionMenuForId = null) }
    }

    private fun onEditSectionClick(sectionId: Long) {
        val section = _uiState.value.sectionItems.firstOrNull { it.id == sectionId } ?: return
        _uiState.update {
            it.copy(
                openSectionMenuForId = null,
                editingSectionId = sectionId,
                editingSectionName = section.name,
                sectionActionError = null,
                taskActionError = null
            )
        }
    }

    private fun onEditSectionNameChange(value: String) {
        _uiState.update {
            it.copy(
                editingSectionName = value,
                sectionActionError = null,
                taskActionError = null
            )
        }
    }

    private fun onEditSectionDismiss() {
        _uiState.update {
            it.copy(
                editingSectionId = null,
                editingSectionName = "",
                sectionActionError = null,
                taskActionError = null
            )
        }
    }

    private fun onSaveSectionEdit() {
        val editingSectionId = _uiState.value.editingSectionId ?: return
        val name = SectionName.create(_uiState.value.editingSectionName)
            .getOrElse { throwable ->
                _uiState.update { it.copy(sectionActionError = throwable.toValidationMessage()) }
                return
            }
        val previousProject = _uiState.value.project

        _uiState.update { state ->
            val updatedSections = state.sectionItems.map { section ->
                if (section.id == editingSectionId) section.copy(name = name.asString()) else section
            }
            state.copy(
                project = state.project?.copy(sections = updatedSections.map { it.name }),
                sectionItems = updatedSections,
                editingSectionId = null,
                editingSectionName = "",
                sectionActionError = null
            )
        }

        viewModelScope.launch {
            updateSectionNameUseCase(editingSectionId, name)
                .onSuccess {
                    _uiState.update { it.copy(sectionActionError = null) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            project = previousProject,
                            sectionActionError = error.message ?: "Could not update section"
                        )
                    }
                }
        }
    }

    private fun onDeleteSectionClick(sectionId: Long) {
        _uiState.update {
            it.copy(
                openSectionMenuForId = null,
                deleteConfirmSectionId = sectionId,
                sectionActionError = null,
                taskActionError = null
            )
        }
    }

    private fun onDeleteSectionDismiss() {
        _uiState.update { it.copy(deleteConfirmSectionId = null) }
    }

    private fun onDeleteSectionConfirm() {
        val sectionId = _uiState.value.deleteConfirmSectionId ?: return
        val previousProject = _uiState.value.project
        _uiState.update { state ->
            val updatedSections = state.sectionItems.filterNot { it.id == sectionId }
            state.copy(
                project = state.project?.copy(sections = updatedSections.map { it.name }),
                sectionItems = updatedSections,
                deleteConfirmSectionId = null,
                sectionActionError = null
            )
        }

        viewModelScope.launch {
            deleteSectionUseCase(sectionId)
                .onSuccess {
                    _uiState.update { it.copy(sectionActionError = null) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            project = previousProject,
                            sectionActionError = error.message ?: "Could not delete section"
                        )
                    }
                }
        }
    }

    private fun loadTasksForSections() {
        val currentProject = _uiState.value.project ?: return
        _uiState.value.sectionItems.forEach { section ->
            viewModelScope.launch {
                getTasksBySectionUseCase(currentProject.id, section.id)
                    .onSuccess { tasks ->
                        _uiState.update { state ->
                            val mapped = tasks.map {
                                TaskUi(id = it.id, name = it.name, completed = it.completed)
                            }
                            val nextTasksById = state.tasksById.toMutableMap().apply {
                                mapped.forEach { put(it.id.toString(), it) }
                            }
                            val nextSections = state.sectionItems.map {
                                if (it.id == section.id) it.copy(taskIds = mapped.map { t -> t.id.toString() }) else it
                            }
                            state.copy(tasksById = nextTasksById, sectionItems = nextSections)
                        }
                    }
                    .onFailure { error ->
                        _uiState.update {
                            it.copy(taskActionError = error.message ?: "Could not load tasks")
                        }
                    }
            }
        }
    }

    private fun onAddTaskClick(sectionId: Long) {
        _uiState.update {
            it.copy(
                creatingTaskSectionId = sectionId,
                creatingTaskName = "",
                taskActionError = null
            )
        }
    }

    private fun onCreateTaskNameChanged(name: String) {
        _uiState.update { it.copy(creatingTaskName = name, taskActionError = null) }
    }

    private fun onCreateTaskDismissed() {
        _uiState.update {
            it.copy(
                creatingTaskSectionId = null,
                creatingTaskName = "",
                taskActionError = null
            )
        }
    }

    private fun onCreateTaskConfirmed() {
        val state = _uiState.value
        val currentProjectId = state.project?.id ?: return
        val sectionId = state.creatingTaskSectionId ?: return
        val taskName = TaskName.create(state.creatingTaskName)
            .getOrElse { throwable ->
                _uiState.update { it.copy(taskActionError = throwable.toTaskValidationMessage()) }
                return
            }
        _uiState.update { it.copy(isTaskCreateLoading = true, taskActionError = null) }
        viewModelScope.launch {
            createTaskUseCase(currentProjectId, sectionId, taskName)
                .onSuccess { createdTask ->
                    _uiState.update { current ->
                        val updatedProject = current.project?.let { project ->
                            project
                        }
                        val createdTaskUi = TaskUi(
                            id = createdTask.id,
                            name = createdTask.name,
                            completed = createdTask.completed
                        )
                        val nextTasksById = current.tasksById.toMutableMap().apply {
                            put(createdTaskUi.id.toString(), createdTaskUi)
                        }
                        val nextSections = current.sectionItems.map { section ->
                            if (section.id == sectionId) {
                                section.copy(taskIds = section.taskIds + createdTaskUi.id.toString())
                            } else section
                        }
                        current.copy(
                            project = updatedProject,
                            creatingTaskSectionId = null,
                            creatingTaskName = "",
                            taskActionError = null,
                            isTaskCreateLoading = false,
                            tasksById = nextTasksById,
                            sectionItems = nextSections
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            taskActionError = error.message ?: "Could not create task",
                            isTaskCreateLoading = false
                        )
                    }
                }
        }
    }

    private fun onTaskMenuOpened(taskId: Long) {
        _uiState.update { it.copy(openTaskMenuForId = taskId) }
    }

    private fun onTaskMenuDismissed() {
        _uiState.update { it.copy(openTaskMenuForId = null) }
    }

    private fun onEditTaskClick(sectionId: Long, taskId: Long) {
        val section = _uiState.value.sectionItems.firstOrNull { it.id == sectionId } ?: return
        val task = section.taskIds.mapNotNull { _uiState.value.tasksById[it] }.firstOrNull { it.id == taskId } ?: return
        _uiState.update {
            it.copy(
                openTaskMenuForId = null,
                editingTaskSectionId = sectionId,
                editingTaskId = taskId,
                editingTaskName = task.name,
                taskActionError = null
            )
        }
    }

    private fun onEditTaskNameChanged(name: String) {
        _uiState.update { it.copy(editingTaskName = name, taskActionError = null) }
    }

    private fun onEditTaskDismissed() {
        _uiState.update {
            it.copy(
                editingTaskSectionId = null,
                editingTaskId = null,
                editingTaskName = "",
                taskActionError = null
            )
        }
    }

    private fun onEditTaskConfirmed() {
        val state = _uiState.value
        val currentProjectId = state.project?.id ?: return
        val sectionId = state.editingTaskSectionId ?: return
        val taskId = state.editingTaskId ?: return
        val newName = TaskName.create(state.editingTaskName)
            .getOrElse { throwable ->
                _uiState.update { it.copy(taskActionError = throwable.toTaskValidationMessage()) }
                return
            }
        _uiState.update { it.copy(isTaskEditLoading = true, taskActionError = null) }
        viewModelScope.launch {
            updateTaskUseCase(currentProjectId, sectionId, taskId, newName)
                .onSuccess { updatedTask ->
                    _uiState.update { current ->
                        val updatedProject = current.project?.let { project ->
                            project
                        }
                        val nextTasksById = current.tasksById.toMutableMap().apply {
                            put(taskId.toString(), TaskUi(taskId, updatedTask.name, updatedTask.completed))
                        }
                        current.copy(
                            project = updatedProject,
                            editingTaskSectionId = null,
                            editingTaskId = null,
                            editingTaskName = "",
                            taskActionError = null,
                            isTaskEditLoading = false,
                            tasksById = nextTasksById
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            taskActionError = error.message ?: "Could not update task",
                            isTaskEditLoading = false
                        )
                    }
                }
        }
    }

    private fun onDeleteTaskClick(sectionId: Long, taskId: Long) {
        _uiState.update {
            it.copy(
                openTaskMenuForId = null,
                deleteConfirmTaskSectionId = sectionId,
                deleteConfirmTaskId = taskId,
                taskActionError = null
            )
        }
    }

    private fun onDeleteTaskDismissed() {
        _uiState.update { it.copy(deleteConfirmTaskSectionId = null, deleteConfirmTaskId = null, taskActionError = null) }
    }

    private fun onDeleteTaskConfirmed() {
        val state = _uiState.value
        val currentProjectId = state.project?.id ?: return
        val sectionId = state.deleteConfirmTaskSectionId ?: return
        val taskId = state.deleteConfirmTaskId ?: return
        _uiState.update { it.copy(isTaskDeleteLoading = true, taskActionError = null) }
        viewModelScope.launch {
            deleteTaskUseCase(currentProjectId, sectionId, taskId)
                .onSuccess {
                    _uiState.update { current ->
                        val updatedProject = current.project?.let { project ->
                            project
                        }
                        val nextTasksById = current.tasksById.toMutableMap().apply { remove(taskId.toString()) }
                        val nextSections = current.sectionItems.map { section ->
                            if (section.id == sectionId) {
                                section.copy(taskIds = section.taskIds.filterNot { it == taskId.toString() })
                            } else section
                        }
                        current.copy(
                            project = updatedProject,
                            deleteConfirmTaskSectionId = null,
                            deleteConfirmTaskId = null,
                            taskActionError = null,
                            isTaskDeleteLoading = false,
                            tasksById = nextTasksById,
                            sectionItems = nextSections
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            taskActionError = error.message ?: "Could not delete task",
                            isTaskDeleteLoading = false
                        )
                    }
                }
        }
    }

    private fun onTaskCompletionToggled(sectionId: Long, taskId: Long) {
        // TODO: Persist task completion once backend endpoint supports completion updates.
        // FIXME: Current backend task update endpoint only accepts `name`, not completion status.
        _uiState.update { current ->
            val updatedProject = current.project?.let { project ->
                project
            }
            val nextTasksById = current.tasksById.toMutableMap()
            val key = taskId.toString()
            val task = nextTasksById[key]
            if (task != null) {
                nextTasksById[key] = task.copy(completed = !task.completed)
            }
            current.copy(project = updatedProject, tasksById = nextTasksById)
        }
    }
}

