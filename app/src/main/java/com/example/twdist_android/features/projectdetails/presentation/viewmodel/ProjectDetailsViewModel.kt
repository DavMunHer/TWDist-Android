package com.example.twdist_android.features.projectdetails.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.features.projectdetails.application.usecases.GetProjectByIdUseCase
import com.example.twdist_android.features.projectdetails.presentation.mapper.toDetailsUi
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val getProjectByIdUseCase: GetProjectByIdUseCase
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
                        it.copy(isLoading = false, project = aggregate.toDetailsUi())
                    }
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
}
