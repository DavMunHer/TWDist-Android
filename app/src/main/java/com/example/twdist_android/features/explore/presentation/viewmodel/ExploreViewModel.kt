package com.example.twdist_android.features.explore.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.features.explore.application.usecases.ChangeProjectFavoriteUseCase
import com.example.twdist_android.features.explore.application.usecases.CreateProjectUseCase
import com.example.twdist_android.features.explore.application.usecases.DeleteProjectUseCase
import com.example.twdist_android.features.explore.application.usecases.GetProjectsUseCase
import com.example.twdist_android.features.explore.presentation.event.ExploreEvent
import com.example.twdist_android.features.explore.presentation.mapper.CreateProjectFormData
import com.example.twdist_android.features.explore.presentation.mapper.toProjectName
import com.example.twdist_android.features.explore.presentation.mapper.toUiError
import com.example.twdist_android.features.explore.presentation.mapper.toUi
import com.example.twdist_android.features.explore.presentation.model.ExploreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val changeProjectFavoriteUseCase: ChangeProjectFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState
    private val favoriteRequestVersion = mutableMapOf<Long, Int>()

    init {
        loadProjects()
    }

    fun handleEvent(event: ExploreEvent) {
        when (event) {
            is ExploreEvent.ToggleExpanded -> toggleExpanded()
            is ExploreEvent.CreateProject -> createProject(event.name)
            is ExploreEvent.LoadProjects -> loadProjects()
            is ExploreEvent.ToggleProjectFavorite -> toggleProjectFavorite(event.projectId)
            is ExploreEvent.ClearValidationErrors -> clearValidationErrors()
            is ExploreEvent.ShowDeleteProjectConfirmation -> showDeleteConfirmation(event.projectId)
            is ExploreEvent.DismissDeleteProjectConfirmation -> dismissDeleteConfirmation()
            is ExploreEvent.ConfirmDeleteProject -> confirmDeleteProject()
        }
    }

    private fun toggleExpanded() {
        _uiState.update { it.copy(isExpanded = !it.isExpanded) }
    }

    private fun clearValidationErrors() {
        _uiState.update { it.copy(projectNameError = null) }
    }

    private fun showDeleteConfirmation(projectId: Long) {
        _uiState.update { state ->
            val project = state.projects.find { it.id == projectId }
            state.copy(projectPendingDelete = project)
        }
    }

    private fun dismissDeleteConfirmation() {
        _uiState.update { it.copy(projectPendingDelete = null) }
    }

    private fun confirmDeleteProject() {
        val pending = _uiState.value.projectPendingDelete ?: return
        val projectId = pending.id
        viewModelScope.launch {
            _uiState.update {
                it.copy(projectPendingDelete = null, error = null)
            }
            deleteProjectUseCase(projectId)
                .onSuccess {
                    loadProjects()
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(error = e.message, isLoading = false)
                    }
                }
        }
    }

    private fun toggleProjectFavorite(projectId: Long) {
        val currentProject = _uiState.value.projects.firstOrNull { it.id == projectId } ?: return
        val nextFavorite = !currentProject.isFavorite
        val version = (favoriteRequestVersion[projectId] ?: 0) + 1
        favoriteRequestVersion[projectId] = version

        _uiState.update { state ->
            state.copy(
                projects = state.projects.map { project ->
                    if (project.id == projectId) project.copy(isFavorite = nextFavorite) else project
                },
                error = null
            )
        }

        viewModelScope.launch {
            changeProjectFavoriteUseCase(projectId, nextFavorite)
                .onFailure { e ->
                    if (favoriteRequestVersion[projectId] == version) {
                        _uiState.update { state ->
                            state.copy(
                                projects = state.projects.map { project ->
                                    if (project.id == projectId) {
                                        project.copy(isFavorite = !nextFavorite)
                                    } else {
                                        project
                                    }
                                },
                                error = e.message
                            )
                        }
                    }
                }
                .onSuccess {
                    if (favoriteRequestVersion[projectId] == version) {
                        favoriteRequestVersion.remove(projectId)
                    }
                }
            }
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getProjectsUseCase().onSuccess { list ->
                favoriteRequestVersion.clear()
                _uiState.update {
                    it.copy(
                        projects = list.map { p -> p.toUi() },
                        isLoading = false,
                        error = null
                    )
                }
            }.onFailure { e ->
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    private fun createProject(name: String) {
        // Validate project name immediately (like auth does on submit)
        val formData = CreateProjectFormData(name)
        val projectNameError = formData.toProjectName().toUiError()

        if (projectNameError != null) {
            _uiState.update {
                it.copy(
                    projectNameError = projectNameError,
                    isLoading = false
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    projectNameError = null
                )
            }

            // Safe to call getOrThrow() after validation above
            val projectName = formData.toProjectName().getOrThrow()
            createProjectUseCase(projectName)
                .onSuccess {
                    loadProjects()
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
        }
    }
}
