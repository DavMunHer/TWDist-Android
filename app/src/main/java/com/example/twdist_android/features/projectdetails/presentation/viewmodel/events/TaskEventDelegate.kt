package com.example.twdist_android.features.projectdetails.presentation.viewmodel.events

import com.example.twdist_android.features.projectdetails.domain.model.TaskName
import com.example.twdist_android.features.projectdetails.presentation.event.TaskEvent
import com.example.twdist_android.features.projectdetails.presentation.model.TaskCompletionUndo
import com.example.twdist_android.features.projectdetails.presentation.model.TaskUi
import com.example.twdist_android.features.projectdetails.presentation.viewmodel.toTaskValidationMessage
import java.time.LocalDate

class TaskEventDelegate(
    private val context: ProjectDetailsMutationContext
) {
    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.AddTaskClicked -> onAddTaskClick(event.sectionId)
            is TaskEvent.CreateTaskNameChanged -> onCreateTaskNameChanged(event.name)
            TaskEvent.CreateTaskConfirmed -> onCreateTaskConfirmed()
            TaskEvent.CreateTaskDismissed -> onCreateTaskDismissed()
            is TaskEvent.TaskMenuOpened -> onTaskMenuOpened(event.taskId)
            TaskEvent.TaskMenuDismissed -> onTaskMenuDismissed()
            is TaskEvent.EditTaskClicked -> onEditTaskClick(event.sectionId, event.taskId)
            is TaskEvent.EditTaskNameChanged -> onEditTaskNameChanged(event.name)
            TaskEvent.EditTaskConfirmed -> onEditTaskConfirmed()
            TaskEvent.EditTaskDismissed -> onEditTaskDismissed()
            is TaskEvent.DeleteTaskClicked -> onDeleteTaskClick(event.sectionId, event.taskId)
            TaskEvent.DeleteTaskConfirmed -> onDeleteTaskConfirmed()
            TaskEvent.DeleteTaskDismissed -> onDeleteTaskDismissed()
            is TaskEvent.TaskCompletionToggled -> onTaskCompletionToggled(event.sectionId, event.taskId)
            is TaskEvent.TaskCompletionUndoHandled -> onTaskCompletionUndoHandled(event.undo)
            TaskEvent.TaskSnackbarShown -> onTaskSnackbarShown()
        }
    }

    private fun onAddTaskClick(sectionId: Long) {
        context.updateState {
            it.copy(
                creatingTaskSectionId = sectionId,
                creatingTaskName = "",
                taskActionError = null
            )
        }
    }

    private fun onCreateTaskNameChanged(name: String) {
        context.updateState { it.copy(creatingTaskName = name, taskActionError = null) }
    }

    private fun onCreateTaskDismissed() {
        context.updateState {
            it.copy(
                creatingTaskSectionId = null,
                creatingTaskName = "",
                taskActionError = null
            )
        }
    }

    private fun onCreateTaskConfirmed() {
        val state = context.state
        val currentProjectId = state.project?.id ?: return
        val sectionId = state.creatingTaskSectionId ?: return
        val taskName = TaskName.create(state.creatingTaskName)
            .getOrElse { throwable ->
                context.updateState { it.copy(taskActionError = throwable.toTaskValidationMessage()) }
                return
            }
        context.updateState { it.copy(isTaskCreateLoading = true, taskActionError = null) }
        context.launch {
            context.createTask(currentProjectId, sectionId, taskName)
                .onSuccess { createdTask ->
                    context.updateState { current ->
                        val updatedProject = current.project?.let { project ->
                            project
                        }
                        val createdTaskUi = TaskUi(
                            id = createdTask.id,
                            name = createdTask.name,
                            completed = createdTask.completed,
                            description = createdTask.description,
                            startDate = createdTask.startDate,
                            endDate = createdTask.endDate
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
                    context.updateState {
                        it.copy(
                            taskActionError = error.message ?: "Could not create task",
                            isTaskCreateLoading = false
                        )
                    }
                }
        }
    }

    private fun onTaskMenuOpened(taskId: Long) {
        context.updateState { it.copy(openTaskMenuForId = taskId) }
    }

    private fun onTaskMenuDismissed() {
        context.updateState { it.copy(openTaskMenuForId = null) }
    }

    private fun onEditTaskClick(sectionId: Long, taskId: Long) {
        val section = context.state.sectionItems.firstOrNull { it.id == sectionId } ?: return
        val task = section.taskIds.mapNotNull { context.state.tasksById[it] }.firstOrNull { it.id == taskId } ?: return
        context.updateState {
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
        context.updateState { it.copy(editingTaskName = name, taskActionError = null) }
    }

    private fun onEditTaskDismissed() {
        context.updateState {
            it.copy(
                editingTaskSectionId = null,
                editingTaskId = null,
                editingTaskName = "",
                taskActionError = null
            )
        }
    }

    private fun onEditTaskConfirmed() {
        val state = context.state
        val currentProjectId = state.project?.id ?: return
        val sectionId = state.editingTaskSectionId ?: return
        val taskId = state.editingTaskId ?: return
        val newName = TaskName.create(state.editingTaskName)
            .getOrElse { throwable ->
                context.updateState { it.copy(taskActionError = throwable.toTaskValidationMessage()) }
                return
            }
        context.updateState { it.copy(isTaskEditLoading = true, taskActionError = null) }
        context.launch {
            context.updateTask(
                projectId = currentProjectId,
                sectionId = sectionId,
                taskId = taskId,
                name = newName
            )
                .onSuccess { updatedTask ->
                    context.updateState { current ->
                        val updatedProject = current.project?.let { project ->
                            project
                        }
                        val nextTasksById = current.tasksById.toMutableMap().apply {
                            put(
                                taskId.toString(),
                                TaskUi(
                                    id = taskId,
                                    name = updatedTask.name,
                                    completed = updatedTask.completed,
                                    description = updatedTask.description,
                                    startDate = updatedTask.startDate,
                                    endDate = updatedTask.endDate
                                )
                            )
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
                    context.updateState {
                        it.copy(
                            taskActionError = error.message ?: "Could not update task",
                            isTaskEditLoading = false
                        )
                    }
                }
        }
    }

    private fun onDeleteTaskClick(sectionId: Long, taskId: Long) {
        context.updateState {
            it.copy(
                openTaskMenuForId = null,
                deleteConfirmTaskSectionId = sectionId,
                deleteConfirmTaskId = taskId,
                taskActionError = null
            )
        }
    }

    private fun onDeleteTaskDismissed() {
        context.updateState { it.copy(deleteConfirmTaskSectionId = null, deleteConfirmTaskId = null, taskActionError = null) }
    }

    private fun onDeleteTaskConfirmed() {
        val state = context.state
        val currentProjectId = state.project?.id ?: return
        val sectionId = state.deleteConfirmTaskSectionId ?: return
        val taskId = state.deleteConfirmTaskId ?: return
        context.updateState { it.copy(isTaskDeleteLoading = true, taskActionError = null) }
        context.launch {
            context.deleteTask(currentProjectId, sectionId, taskId)
                .onSuccess {
                    context.updateState { current ->
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
                    context.updateState {
                        it.copy(
                            taskActionError = error.message ?: "Could not delete task",
                            isTaskDeleteLoading = false
                        )
                    }
                }
        }
    }

    private fun onTaskCompletionToggled(sectionId: Long, taskId: Long) {
        val state = context.state
        val currentProjectId = state.project?.id ?: return
        val taskKey = taskId.toString()
        val task = state.tasksById[taskKey] ?: return

        context.updateState { current ->
            current.copy(
                tasksById = current.tasksById.toMutableMap().apply { remove(taskKey) },
                sectionItems = current.sectionItems.map { section ->
                    if (section.id == sectionId) {
                        section.copy(taskIds = section.taskIds.filterNot { it == taskKey })
                    } else {
                        section
                    }
                },
                taskCompletionUndo = TaskCompletionUndo(sectionId = sectionId, task = task),
                taskActionError = null,
                taskSnackbarMessage = null
            )
        }

        context.launch {
            context.completeTask(
                projectId = currentProjectId,
                sectionId = sectionId,
                taskId = taskId,
                completedDate = LocalDate.now()
            ).onSuccess { updatedTask ->
                if (!updatedTask.completed) {
                    context.updateState { current ->
                        val nextTasksById = current.tasksById.toMutableMap().apply {
                            put(
                                taskKey,
                                TaskUi(
                                    id = updatedTask.id,
                                    name = updatedTask.name,
                                    completed = updatedTask.completed,
                                    description = updatedTask.description,
                                    startDate = updatedTask.startDate,
                                    endDate = updatedTask.endDate
                                )
                            )
                        }
                        val nextSections = current.sectionItems.map { section ->
                            if (section.id == sectionId && taskKey !in section.taskIds) {
                                section.copy(taskIds = section.taskIds + taskKey)
                            } else {
                                section
                            }
                        }
                        current.copy(tasksById = nextTasksById, sectionItems = nextSections)
                    }
                }
            }.onFailure { error ->
                context.updateState { current ->
                    val nextTasksById = current.tasksById.toMutableMap().apply {
                        put(taskKey, task)
                    }
                    val nextSections = current.sectionItems.map { section ->
                        if (section.id == sectionId && taskKey !in section.taskIds) {
                            section.copy(taskIds = section.taskIds + taskKey)
                        } else {
                            section
                        }
                    }
                    current.copy(
                        tasksById = nextTasksById,
                        sectionItems = nextSections,
                        taskCompletionUndo = null,
                        taskActionError = error.message ?: "Could not update task",
                        taskSnackbarMessage = "Could not update task"
                    )
                }
            }
        }
    }

    private fun onTaskCompletionUndoHandled(undo: Boolean) {
        val pendingUndo = context.state.taskCompletionUndo ?: return
        context.updateState { it.copy(taskCompletionUndo = null) }
        if (!undo) return

        val projectId = context.state.project?.id ?: return
        val task = pendingUndo.task
        val taskKey = task.id.toString()
        val sectionId = pendingUndo.sectionId

        context.updateState { current ->
            val nextTasksById = current.tasksById.toMutableMap().apply {
                put(taskKey, task.copy(completed = false))
            }
            val nextSections = current.sectionItems.map { section ->
                if (section.id == sectionId && taskKey !in section.taskIds) {
                    section.copy(taskIds = section.taskIds + taskKey)
                } else section
            }
            current.copy(tasksById = nextTasksById, sectionItems = nextSections, taskActionError = null)
        }

        context.launch {
            context.completeTask(
                projectId = projectId,
                sectionId = sectionId,
                taskId = task.id,
                completedDate = null
            ).onSuccess { updatedTask ->
                if (updatedTask.completed) {
                    context.updateState { current ->
                        val nextTasksById = current.tasksById.toMutableMap().apply {
                            remove(taskKey)
                        }
                        val nextSections = current.sectionItems.map { section ->
                            if (section.id == sectionId) {
                                section.copy(taskIds = section.taskIds.filterNot { it == taskKey })
                            } else section
                        }
                        current.copy(
                            tasksById = nextTasksById,
                            sectionItems = nextSections,
                            taskActionError = "Could not undo task completion",
                            taskSnackbarMessage = "Could not undo task completion"
                        )
                    }
                }
            }.onFailure { error ->
                context.updateState { current ->
                    val nextTasksById = current.tasksById.toMutableMap().apply {
                        remove(taskKey)
                    }
                    val nextSections = current.sectionItems.map { section ->
                        if (section.id == sectionId) {
                            section.copy(taskIds = section.taskIds.filterNot { it == taskKey })
                        } else section
                    }
                    current.copy(
                        tasksById = nextTasksById,
                        sectionItems = nextSections,
                        taskActionError = error.message ?: "Could not undo task completion",
                        taskSnackbarMessage = "Could not undo task completion"
                    )
                }
            }
        }
    }

    private fun onTaskSnackbarShown() {
        context.updateState { it.copy(taskSnackbarMessage = null) }
    }
}
