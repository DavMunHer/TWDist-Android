package com.example.twdist_android.features.favorite.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.features.explore.application.usecases.ChangeProjectFavoriteUseCase
import com.example.twdist_android.features.explore.application.usecases.GetProjectsUseCase
import com.example.twdist_android.features.explore.presentation.mapper.toUi
import com.example.twdist_android.features.explore.presentation.model.ProjectUi
import com.example.twdist_android.features.favorite.presentation.event.FavoriteProjectsEvent
import com.example.twdist_android.features.favorite.presentation.model.FavoriteProjectsUiState
import com.example.twdist_android.features.favorite.presentation.model.FavoriteUndoAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteProjectsViewModel @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val changeProjectFavoriteUseCase: ChangeProjectFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteProjectsUiState())
    val uiState: StateFlow<FavoriteProjectsUiState> = _uiState

    private val requestVersion = mutableMapOf<Long, Int>()
    private val pendingUndoActions = mutableMapOf<Long, PendingUndo>()
    private var nextUndoToken: Long = 0

    init {
        loadProjects()
    }

    fun handleEvent(event: FavoriteProjectsEvent) {
        when (event) {
            FavoriteProjectsEvent.LoadProjects -> loadProjects()
            is FavoriteProjectsEvent.UnfavoriteProject -> unfavoriteProject(event.projectId)
            is FavoriteProjectsEvent.UndoUnfavorite -> undoUnfavorite(event.token)
            is FavoriteProjectsEvent.UndoMessageHandled -> handleUndoMessageDismiss(event.token)
        }
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getProjectsUseCase()
                .onSuccess { projects ->
                    requestVersion.clear()
                    pendingUndoActions.clear()
                    _uiState.update {
                        it.copy(
                            projects = projects.map { project -> project.toUi() }.filter(ProjectUi::isFavorite),
                            isLoading = false,
                            error = null,
                            pendingUndo = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun unfavoriteProject(projectId: Long) {
        val state = _uiState.value
        val projectIndex = state.projects.indexOfFirst { it.id == projectId }
        if (projectIndex < 0) return

        val project = state.projects[projectIndex]
        val token = ++nextUndoToken
        val version = (requestVersion[projectId] ?: 0) + 1
        requestVersion[projectId] = version
        pendingUndoActions[token] = PendingUndo(project = project, index = projectIndex)

        _uiState.update {
            it.copy(
                projects = it.projects.filterNot { favorite -> favorite.id == projectId },
                error = null,
                pendingUndo = FavoriteUndoAction(token = token)
            )
        }

        viewModelScope.launch {
            changeProjectFavoriteUseCase(projectId, false)
                .onFailure { error ->
                    if (requestVersion[projectId] == version) {
                        val pendingUndo = pendingUndoActions.remove(token)
                        if (pendingUndo != null) {
                            restoreProject(pendingUndo.project, pendingUndo.index)
                        }
                        _uiState.update {
                            if (it.pendingUndo?.token == token) {
                                it.copy(error = error.message, pendingUndo = null)
                            } else {
                                it.copy(error = error.message)
                            }
                        }
                    }
                }
        }
    }

    private fun undoUnfavorite(token: Long) {
        val pendingUndo = pendingUndoActions.remove(token) ?: return
        val project = pendingUndo.project
        val version = (requestVersion[project.id] ?: 0) + 1
        requestVersion[project.id] = version

        restoreProject(project, pendingUndo.index)
        _uiState.update {
            if (it.pendingUndo?.token == token) {
                it.copy(pendingUndo = null, error = null)
            } else {
                it.copy(error = null)
            }
        }

        viewModelScope.launch {
            changeProjectFavoriteUseCase(project.id, true)
                .onFailure { error ->
                    if (requestVersion[project.id] == version) {
                        _uiState.update {
                            it.copy(
                                projects = it.projects.filterNot { favorite -> favorite.id == project.id },
                                error = error.message
                            )
                        }
                    }
                }
        }
    }

    private fun handleUndoMessageDismiss(token: Long) {
        pendingUndoActions.remove(token)?.let { pending ->
            requestVersion.remove(pending.project.id)
        }
        _uiState.update {
            if (it.pendingUndo?.token == token) {
                it.copy(pendingUndo = null)
            } else {
                it
            }
        }
    }

    private fun restoreProject(project: ProjectUi, index: Int) {
        _uiState.update { state ->
            if (state.projects.any { it.id == project.id }) {
                state
            } else {
                val insertAt = index.coerceAtMost(state.projects.size)
                val mutableProjects = state.projects.toMutableList()
                mutableProjects.add(insertAt, project)
                state.copy(projects = mutableProjects)
            }
        }
    }

    private data class PendingUndo(
        val project: ProjectUi,
        val index: Int
    )
}
