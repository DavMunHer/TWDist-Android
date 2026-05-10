package com.example.twdist_android.features.upcoming.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.core.ui.components.task.TaskRowState
import com.example.twdist_android.features.upcoming.application.usecases.CompleteUpcomingTaskUseCase
import com.example.twdist_android.features.upcoming.application.usecases.GetUpcomingTasksUseCase
import com.example.twdist_android.features.upcoming.application.usecases.RefreshUpcomingTasksUseCase
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
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    private val getUpcomingTasksUseCase: GetUpcomingTasksUseCase,
    private val refreshUpcomingTasksUseCase: RefreshUpcomingTasksUseCase,
    private val completeUpcomingTaskUseCase: CompleteUpcomingTaskUseCase,
    private val undoCompleteUpcomingTaskUseCase: UndoCompleteUpcomingTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UpcomingUiState())
    val uiState: StateFlow<UpcomingUiState> = _uiState

    private val _events = MutableSharedFlow<UpcomingUiEvent>()
    val events: SharedFlow<UpcomingUiEvent> = _events

    private val today: LocalDate = LocalDate.now()
    private val endOfMonth: LocalDate = today.with(TemporalAdjusters.lastDayOfMonth())

    init {
        viewModelScope.launch {
            getUpcomingTasksUseCase(from = today, to = endOfMonth).collect { tasks ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        items = buildListItems(today, endOfMonth, tasks)
                    )
                }
            }
        }
        refreshTasks(showLoading = true)
    }

    /**
     * @param showLoading When true (initial load / pull equivalent), clears the list behind a fullscreen spinner.
     *   When false (e.g. screen resume), keep showing cached rows — Room Flow will refresh when persist completes.
     */
    fun refreshTasks(showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) {
                _uiState.update { it.copy(isLoading = true, error = null) }
            }
            refreshUpcomingTasksUseCase(from = today, to = endOfMonth)
                .onSuccess {
                    // Room Flow does not always re-emit (e.g. empty API body -> no DAO write): clear spinner anyway.
                    if (showLoading) {
                        _uiState.update { it.copy(isLoading = false) }
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
            val taskDate = _uiState.value.items
                .filterIsInstance<UpcomingListItem.Task>()
                .firstOrNull { it.state.id == task.id }
                ?.date

            completeUpcomingTaskUseCase(
                projectId = task.projectId,
                sectionId = task.sectionId,
                taskId = task.id
            ).onSuccess {
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
            ).onFailure { throwable ->
                _uiState.update { state -> state.copy(error = throwable.message) }
            }
        }
    }

    private fun buildListItems(
        from: LocalDate,
        to: LocalDate,
        tasks: List<UpcomingTask>
    ): List<UpcomingListItem> {
        val byDate = tasks.groupBy { it.startDate }
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
}
