package com.example.twdist_android.features.upcoming.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.core.events.TaskEventBus
import com.example.twdist_android.core.ui.components.task.TaskRowState
import com.example.twdist_android.features.upcoming.application.usecases.CompleteUpcomingTaskUseCase
import com.example.twdist_android.features.upcoming.application.usecases.GetUpcomingTasksUseCase
import com.example.twdist_android.features.upcoming.application.usecases.UndoCompleteUpcomingTaskUseCase
import com.example.twdist_android.features.upcoming.domain.model.UpcomingTask
import com.example.twdist_android.features.upcoming.presentation.model.UpcomingListItem
import com.example.twdist_android.features.upcoming.presentation.model.UpcomingUiEvent
import com.example.twdist_android.features.upcoming.presentation.model.UpcomingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    private val getUpcomingTasksUseCase: GetUpcomingTasksUseCase,
    private val completeUpcomingTaskUseCase: CompleteUpcomingTaskUseCase,
    private val undoCompleteUpcomingTaskUseCase: UndoCompleteUpcomingTaskUseCase,
    private val taskEventBus: TaskEventBus
) : ViewModel() {

    private val _uiState = MutableStateFlow(UpcomingUiState())
    val uiState: StateFlow<UpcomingUiState> = _uiState

    private val _events = MutableSharedFlow<UpcomingUiEvent>()
    val events: SharedFlow<UpcomingUiEvent> = _events

    init {
        loadUpcomingTasks()
        viewModelScope.launch {
            taskEventBus.taskEndDateUpdated.collect { event ->
                applyEndDateUpdate(event.taskId, event.newEndDate)
            }
        }
    }

    fun loadUpcomingTasks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val today = LocalDate.now()
            val endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth())

            getUpcomingTasksUseCase(from = today, to = endOfMonth)
                .onSuccess { tasks ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            items = buildListItems(today, endOfMonth, tasks)
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(isLoading = false, error = throwable.message) }
                }
        }
    }

    fun onVisibleDateChanged(date: LocalDate) {
        _uiState.update {
            it.copy(
                visibleDate = date,
                weekStart = date.with(WeekFields.ISO.dayOfWeek(), 1)
            )
        }
    }

    fun onTaskCompleted(task: TaskRowState) {
        if (task.isCompleted) return

        viewModelScope.launch {
            // Capture the task's end date before removing it from the list so that
            // undoTaskCompleted can reinsert it under the correct day header.
            val taskDate = _uiState.value.items
                .filterIsInstance<UpcomingListItem.Task>()
                .firstOrNull { it.state.id == task.id }
                ?.date

            completeUpcomingTaskUseCase(
                projectId = task.projectId,
                sectionId = task.sectionId,
                taskId = task.id
            ).onSuccess {
                _uiState.update { state ->
                    state.copy(items = state.items.filterNot {
                        it is UpcomingListItem.Task && it.state.id == task.id
                    })
                }
                if (taskDate != null) {
                    _events.emit(UpcomingUiEvent.TaskCompleted(task, taskDate))
                }
            }.onFailure { throwable ->
                _uiState.update { state -> state.copy(error = throwable.message) }
            }
        }
    }

    fun undoTaskCompleted(task: TaskRowState, date: LocalDate) {
        viewModelScope.launch {
            undoCompleteUpcomingTaskUseCase(
                projectId = task.projectId,
                sectionId = task.sectionId,
                taskId = task.id
            ).onSuccess {
                _uiState.update { state ->
                    if (state.items.any { it is UpcomingListItem.Task && it.state.id == task.id }) {
                        state
                    } else {
                        val restored = UpcomingListItem.Task(task.copy(isCompleted = false), date)
                        state.copy(items = reinsertTask(state.items, restored))
                    }
                }
            }.onFailure { throwable ->
                _uiState.update { state -> state.copy(error = throwable.message) }
            }
        }
    }

    private fun applyEndDateUpdate(taskId: Long, newEndDate: LocalDate?) {
        val today = LocalDate.now()
        val endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth())

        _uiState.update { state ->
            val existingTask = state.items
                .filterIsInstance<UpcomingListItem.Task>()
                .firstOrNull { it.state.id == taskId }
                ?: return@update state

            val withoutTask = state.items.filterNot {
                it is UpcomingListItem.Task && it.state.id == taskId
            }

            if (newEndDate == null || newEndDate.isBefore(today) || newEndDate.isAfter(endOfMonth)) {
                return@update state.copy(items = withoutTask)
            }

            val movedTask = existingTask.copy(date = newEndDate)
            state.copy(items = reinsertTask(withoutTask, movedTask))
        }
    }

    private fun buildListItems(
        from: LocalDate,
        to: LocalDate,
        tasks: List<UpcomingTask>
    ): List<UpcomingListItem> {
        val byDate = tasks.groupBy { it.endDate }
        val result = mutableListOf<UpcomingListItem>()
        var current = from
        while (!current.isAfter(to)) {
            result.add(UpcomingListItem.Header(current))
            byDate[current]?.forEach { task ->
                result.add(
                    UpcomingListItem.Task(
                        state = TaskRowState(
                            id = task.id,
                            projectId = task.projectId,
                            sectionId = task.sectionId,
                            title = task.name,
                            projectName = task.projectName
                        ),
                        date = current
                    )
                )
            }
            current = current.plusDays(1)
        }
        return result
    }

    private fun reinsertTask(
        items: List<UpcomingListItem>,
        task: UpcomingListItem.Task
    ): List<UpcomingListItem> {
        val result = items.toMutableList()
        val headerIdx = result.indexOfFirst { it is UpcomingListItem.Header && it.date == task.date }
        if (headerIdx == -1) return result
        var insertIdx = headerIdx + 1
        while (insertIdx < result.size && result[insertIdx] is UpcomingListItem.Task) {
            insertIdx++
        }
        result.add(insertIdx, task)
        return result
    }
}
