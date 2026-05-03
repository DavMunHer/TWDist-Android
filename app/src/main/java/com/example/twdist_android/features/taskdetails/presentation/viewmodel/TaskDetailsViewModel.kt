package com.example.twdist_android.features.taskdetails.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.core.events.TaskEventBus
import com.example.twdist_android.features.projectdetails.application.usecases.task.CompleteTaskUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.task.DeleteTaskUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.project.GetProjectByIdUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.task.GetTasksBySectionUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.task.UpdateTaskUseCase
import com.example.twdist_android.features.projectdetails.domain.model.TaskName
import com.example.twdist_android.features.taskdetails.presentation.event.TaskDetailsEvent
import com.example.twdist_android.features.taskdetails.presentation.model.TaskDetailsUi
import com.example.twdist_android.features.taskdetails.presentation.model.TaskDetailsUiEvent
import com.example.twdist_android.features.taskdetails.presentation.model.TaskDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getTasksBySectionUseCase: GetTasksBySectionUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val taskEventBus: TaskEventBus
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailsUiState())
    val uiState: StateFlow<TaskDetailsUiState> = _uiState
    private val _events = MutableSharedFlow<TaskDetailsUiEvent>()
    val events: SharedFlow<TaskDetailsUiEvent> = _events

    fun loadTaskDetails(projectId: Long, sectionId: Long, taskId: Long) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    projectId = projectId,
                    sectionId = sectionId,
                    taskId = taskId
                )
            }

            val projectResult = getProjectByIdUseCase(projectId)
            val tasksResult = getTasksBySectionUseCase(projectId, sectionId)

            if (projectResult.isFailure || tasksResult.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = projectResult.exceptionOrNull()?.message
                            ?: tasksResult.exceptionOrNull()?.message
                            ?: "Could not load task details"
                    )
                }
                return@launch
            }

            val projectName = projectResult.getOrThrow().project.name.asString()
            val task = tasksResult.getOrThrow().firstOrNull { it.id == taskId }
            if (task == null) {
                _uiState.update {
                    it.copy(isLoading = false, projectName = projectName, error = "Task not found")
                }
                return@launch
            }

            val taskUi = TaskDetailsUi(
                id = task.id,
                name = task.name,
                completed = task.completed,
                description = task.description,
                startDate = task.startDate,
                endDate = task.endDate
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    projectName = projectName,
                    task = taskUi,
                    startDate = normalizeDisplayDate(taskUi.startDate),
                    endDate = normalizeDisplayDate(taskUi.endDate),
                    description = taskUi.description.orEmpty(),
                    saveMessage = null
                )
            }
        }
    }

    fun onEvent(event: TaskDetailsEvent) {
        when (event) {
            TaskDetailsEvent.MenuOpened -> _uiState.update { it.copy(openProjectMenu = true) }
            TaskDetailsEvent.MenuDismissed -> _uiState.update { it.copy(openProjectMenu = false) }
            TaskDetailsEvent.MenuEditClicked -> _uiState.update { it.copy(openProjectMenu = false) }
            TaskDetailsEvent.MenuDeleteClicked -> _uiState.update { it.copy(openProjectMenu = false) }
            TaskDetailsEvent.TaskMenuDismissed -> _uiState.update { it.copy(openTaskMenuForId = null) }
            TaskDetailsEvent.TaskEditDismissed -> _uiState.update {
                it.copy(editingTaskId = null, editingTaskName = "", taskActionError = null, isTaskEditLoading = false)
            }
            is TaskDetailsEvent.TaskMenuOpened -> _uiState.update { it.copy(openTaskMenuForId = event.taskId) }
            is TaskDetailsEvent.TaskCompletionToggled -> onTaskCompletionToggled(event.sectionId, event.taskId)
            is TaskDetailsEvent.TaskEditClicked -> onTaskEditClicked(event.taskId)
            is TaskDetailsEvent.TaskEditNameChanged -> _uiState.update { it.copy(editingTaskName = event.value, taskActionError = null) }
            TaskDetailsEvent.TaskEditConfirmed -> onTaskEditConfirmed()
            is TaskDetailsEvent.TaskDeleteClicked -> _uiState.update {
                it.copy(openTaskMenuForId = null, deleteConfirmTaskId = event.taskId, taskActionError = null)
            }
            TaskDetailsEvent.TaskDeleteDismissed -> _uiState.update { it.copy(deleteConfirmTaskId = null, taskActionError = null) }
            TaskDetailsEvent.TaskDeleteConfirmed -> onTaskDeleteConfirmed()
            is TaskDetailsEvent.StartDateChanged -> _uiState.update { it.copy(startDate = event.value, saveMessage = null) }
            is TaskDetailsEvent.EndDateChanged -> _uiState.update { it.copy(endDate = event.value, saveMessage = null) }
            is TaskDetailsEvent.DescriptionChanged -> _uiState.update { it.copy(description = event.value, saveMessage = null) }
            TaskDetailsEvent.SaveClicked -> saveTaskDetails()
            TaskDetailsEvent.SaveMessageShown -> _uiState.update { it.copy(saveMessage = null) }
        }
    }

    private fun saveTaskDetails() {
        val state = _uiState.value
        val projectId = state.projectId ?: return
        val sectionId = state.sectionId ?: return
        val taskId = state.taskId ?: return
        val currentTask = state.task ?: return

        val taskName = TaskName.create(currentTask.name).getOrElse {
            _uiState.update { current -> current.copy(saveMessage = "Task name is invalid") }
            return
        }

        val startDate = toApiDateWithValidation(state.startDate, "Start Date")
        if (state.startDate.isNotBlank() && startDate == null) return
        val endDate = toApiDateWithValidation(state.endDate, "End Date")
        if (state.endDate.isNotBlank() && endDate == null) return

        _uiState.update { it.copy(isSaving = true, saveMessage = null) }
        viewModelScope.launch {
            updateTaskUseCase(
                projectId = projectId,
                sectionId = sectionId,
                taskId = taskId,
                name = taskName,
                description = state.description.trim().ifBlank { null },
                startDate = startDate,
                endDate = endDate
            ).onSuccess { updatedTask ->
                _uiState.update { current ->
                    current.copy(
                        isSaving = false,
                        task = current.task?.copy(
                            name = updatedTask.name,
                            completed = updatedTask.completed,
                            description = updatedTask.description,
                            startDate = updatedTask.startDate,
                            endDate = updatedTask.endDate
                        ),
                        description = updatedTask.description.orEmpty(),
                        startDate = normalizeDisplayDate(updatedTask.startDate),
                        endDate = normalizeDisplayDate(updatedTask.endDate),
                        saveMessage = "Task details saved"
                    )
                }
                taskEventBus.emitEndDateUpdated(
                    taskId = taskId,
                    newEndDate = updatedTask.endDate?.let {
                        runCatching { LocalDate.parse(it) }.getOrNull()
                    }
                )
                _events.emit(TaskDetailsUiEvent.NavigateBackToProjectDetails)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saveMessage = error.message ?: "Could not save task details"
                    )
                }
            }
        }
    }

    private fun onTaskCompletionToggled(sectionId: Long, taskId: Long) {
        val state = _uiState.value
        val projectId = state.projectId ?: return
        val task = state.task ?: return
        val nextCompleted = !task.completed
        val completedDate = if (nextCompleted) LocalDate.now() else null

        _uiState.update { current ->
            current.copy(task = current.task?.copy(completed = nextCompleted), taskActionError = null)
        }

        viewModelScope.launch {
            completeTaskUseCase(
                projectId = projectId,
                sectionId = sectionId,
                taskId = taskId,
                completedDate = completedDate
            ).onSuccess { updatedTask ->
                _uiState.update { current ->
                    current.copy(
                        task = current.task?.copy(
                            name = updatedTask.name,
                            completed = updatedTask.completed,
                            description = updatedTask.description,
                            startDate = updatedTask.startDate,
                            endDate = updatedTask.endDate
                        ),
                        description = updatedTask.description.orEmpty(),
                        startDate = normalizeDisplayDate(updatedTask.startDate),
                        endDate = normalizeDisplayDate(updatedTask.endDate)
                    )
                }
            }.onFailure { error ->
                _uiState.update { current ->
                    current.copy(
                        task = current.task?.copy(completed = task.completed),
                        taskActionError = error.message ?: "Could not update task"
                    )
                }
            }
        }
    }

    private fun onTaskEditClicked(taskId: Long) {
        val task = _uiState.value.task ?: return
        _uiState.update {
            it.copy(
                openTaskMenuForId = null,
                editingTaskId = taskId,
                editingTaskName = task.name,
                taskActionError = null
            )
        }
    }

    private fun onTaskEditConfirmed() {
        val state = _uiState.value
        val projectId = state.projectId ?: return
        val sectionId = state.sectionId ?: return
        val taskId = state.editingTaskId ?: return
        val newName = TaskName.create(state.editingTaskName).getOrElse {
            _uiState.update { current -> current.copy(taskActionError = "Task name is invalid") }
            return
        }
        val startDate = toApiDate(state.startDate)
        val endDate = toApiDate(state.endDate)

        _uiState.update { it.copy(isTaskEditLoading = true, taskActionError = null) }
        viewModelScope.launch {
            updateTaskUseCase(
                projectId = projectId,
                sectionId = sectionId,
                taskId = taskId,
                name = newName,
                description = state.description.trim().ifBlank { null },
                startDate = startDate,
                endDate = endDate
            ).onSuccess { updatedTask ->
                _uiState.update { current ->
                    current.copy(
                        isTaskEditLoading = false,
                        editingTaskId = null,
                        editingTaskName = "",
                        task = current.task?.copy(
                            name = updatedTask.name,
                            completed = updatedTask.completed,
                            description = updatedTask.description,
                            startDate = updatedTask.startDate,
                            endDate = updatedTask.endDate
                        ),
                        description = updatedTask.description.orEmpty(),
                        startDate = normalizeDisplayDate(updatedTask.startDate),
                        endDate = normalizeDisplayDate(updatedTask.endDate)
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isTaskEditLoading = false,
                        taskActionError = error.message ?: "Could not update task"
                    )
                }
            }
        }
    }

    private fun onTaskDeleteConfirmed() {
        val state = _uiState.value
        val projectId = state.projectId ?: return
        val sectionId = state.sectionId ?: return
        val taskId = state.deleteConfirmTaskId ?: return

        _uiState.update { it.copy(isTaskDeleteLoading = true, taskActionError = null) }
        viewModelScope.launch {
            deleteTaskUseCase(projectId, sectionId, taskId)
                .onSuccess {
                    _uiState.update { current ->
                        current.copy(
                            isTaskDeleteLoading = false,
                            deleteConfirmTaskId = null
                        )
                    }
                    _events.emit(TaskDetailsUiEvent.NavigateBackToProjectDetails)
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isTaskDeleteLoading = false,
                            taskActionError = error.message ?: "Could not delete task"
                        )
                    }
                }
        }
    }

    private fun toApiDateWithValidation(displayDate: String, fieldName: String): String? {
        if (displayDate.isBlank()) return null
        val parsed = toApiDate(displayDate)
        if (parsed == null) {
            _uiState.update { current -> current.copy(saveMessage = "$fieldName must be DD/MM/YYYY") }
        }
        return parsed
    }
}

private val displayDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

private fun normalizeDisplayDate(value: String?): String {
    val raw = value?.trim().orEmpty()
    if (raw.isBlank()) return ""
    val parsed = parseAnyDateToLocalDate(raw) ?: return raw
    return parsed.format(displayDateFormatter)
}

private fun toApiDate(displayDate: String): String? {
    val raw = displayDate.trim()
    if (raw.isBlank()) return null
    return try {
        LocalDate.parse(raw, displayDateFormatter).toString()
    } catch (_: DateTimeParseException) {
        null
    }
}

private fun parseAnyDateToLocalDate(raw: String): LocalDate? {
    return runCatching { LocalDate.parse(raw, displayDateFormatter) }.getOrNull()
        ?: runCatching { LocalDate.parse(raw) }.getOrNull()
        ?: runCatching { OffsetDateTime.parse(raw).toLocalDate() }.getOrNull()
        ?: runCatching { Instant.parse(raw).atZone(ZoneId.systemDefault()).toLocalDate() }.getOrNull()
}
