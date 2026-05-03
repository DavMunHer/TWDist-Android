package com.example.twdist_android.features.today.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.core.ui.components.task.TaskRowState
import com.example.twdist_android.features.today.application.usecases.CompleteTodayTaskUseCase
import com.example.twdist_android.features.today.application.usecases.GetTodayTasksUseCase
import com.example.twdist_android.features.today.application.usecases.UndoCompleteTodayTaskUseCase
import com.example.twdist_android.features.today.presentation.model.TodayUiEvent
import com.example.twdist_android.features.today.presentation.model.TodayUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val getTodayTasksUseCase: GetTodayTasksUseCase,
    private val completeTodayTaskUseCase: CompleteTodayTaskUseCase,
    private val undoCompleteTodayTaskUseCase: UndoCompleteTodayTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TodayUiState(formattedDate = formatTodayDate(LocalDate.now()))
    )
    val uiState: StateFlow<TodayUiState> = _uiState
    private val _events = MutableSharedFlow<TodayUiEvent>()
    val events: SharedFlow<TodayUiEvent> = _events

    init {
        loadTodayTasks()
    }

    fun loadTodayTasks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getTodayTasksUseCase()
                .onSuccess { tasks ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            tasks = tasks.map { task ->
                                TaskRowState(
                                    id = task.id,
                                    projectId = task.projectId,
                                    sectionId = task.sectionId,
                                    title = task.name,
                                    projectName = task.projectName,
                                    isCompleted = false
                                )
                            }
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(isLoading = false, error = throwable.message)
                    }
                }
        }
    }

    fun onTaskCompleted(task: TaskRowState) {
        if (task.isCompleted) return

        viewModelScope.launch {
            completeTodayTaskUseCase(
                projectId = task.projectId,
                sectionId = task.sectionId,
                taskId = task.id
            ).onSuccess {
                _uiState.update { state ->
                    state.copy(tasks = state.tasks.filterNot { it.id == task.id })
                }
                _events.emit(TodayUiEvent.TaskCompleted(task))
            }.onFailure { throwable ->
                _uiState.update { state ->
                    state.copy(error = throwable.message)
                }
            }
        }
    }

    fun undoTaskCompleted(task: TaskRowState) {
        viewModelScope.launch {
            undoCompleteTodayTaskUseCase(
                projectId = task.projectId,
                sectionId = task.sectionId,
                taskId = task.id
            ).onSuccess {
                _uiState.update { state ->
                    if (state.tasks.any { it.id == task.id }) {
                        state
                    } else {
                        state.copy(tasks = listOf(task.copy(isCompleted = false)) + state.tasks)
                    }
                }
            }.onFailure { throwable ->
                _uiState.update { state ->
                    state.copy(error = throwable.message)
                }
            }
        }
    }

    private fun formatTodayDate(today: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("EEEE, dd MMM", Locale.ENGLISH)
        return today.format(formatter)
    }
}
