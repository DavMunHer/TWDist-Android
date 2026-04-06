package com.example.twdist_android.features.projectdetails.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.features.projectdetails.application.usecases.DeleteSectionUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.GetProjectByIdUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.UpdateSectionNameUseCase
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.domain.model.SectionNameError
import com.example.twdist_android.features.projectdetails.domain.model.SectionNameException
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
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val updateSectionNameUseCase: UpdateSectionNameUseCase,
    private val deleteSectionUseCase: DeleteSectionUseCase
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
                            sectionActionError = null
                        )
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

    fun onSectionOptionsClick(sectionId: Long) {
        _uiState.update {
            it.copy(
                openSectionMenuForId = sectionId,
                sectionActionError = null
            )
        }
    }

    fun onSectionOptionsDismiss() {
        _uiState.update { it.copy(openSectionMenuForId = null) }
    }

    fun onEditSectionClick(sectionId: Long) {
        val section = _uiState.value.project?.sections?.firstOrNull { it.id == sectionId } ?: return
        _uiState.update {
            it.copy(
                openSectionMenuForId = null,
                editingSectionId = sectionId,
                editingSectionName = section.name,
                sectionActionError = null
            )
        }
    }

    fun onEditSectionNameChange(value: String) {
        _uiState.update {
            it.copy(
                editingSectionName = value,
                sectionActionError = null
            )
        }
    }

    fun onEditSectionDismiss() {
        _uiState.update {
            it.copy(
                editingSectionId = null,
                editingSectionName = "",
                sectionActionError = null
            )
        }
    }

    fun onSaveSectionEdit() {
        val editingSectionId = _uiState.value.editingSectionId ?: return
        val name = SectionName.create(_uiState.value.editingSectionName)
            .getOrElse { throwable ->
                _uiState.update { it.copy(sectionActionError = throwable.toValidationMessage()) }
                return
            }
        val previousProject = _uiState.value.project

        _uiState.update { state ->
            val updatedProject = state.project?.copy(
                sections = state.project.sections.map { section ->
                    if (section.id == editingSectionId) {
                        section.copy(name = name.asString())
                    } else {
                        section
                    }
                }
            )
            state.copy(
                project = updatedProject,
                editingSectionId = null,
                editingSectionName = "",
                sectionActionError = null
            )
        }

        viewModelScope.launch {
            updateSectionNameUseCase(editingSectionId, name)
                .onSuccess {
                    _uiState.update { state ->
                        val updatedProject = state.project?.copy(
                            sections = state.project.sections.map { section ->
                                if (section.id == editingSectionId) {
                                    section.copy(name = name.asString())
                                } else {
                                    section
                                }
                            }
                        )
                        state.copy(project = updatedProject, sectionActionError = null)
                    }
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

    fun onDeleteSectionClick(sectionId: Long) {
        _uiState.update {
            it.copy(
                openSectionMenuForId = null,
                deleteConfirmSectionId = sectionId,
                sectionActionError = null
            )
        }
    }

    fun onDeleteSectionDismiss() {
        _uiState.update { it.copy(deleteConfirmSectionId = null) }
    }

    fun onDeleteSectionConfirm() {
        val sectionId = _uiState.value.deleteConfirmSectionId ?: return
        val previousProject = _uiState.value.project
        _uiState.update { state ->
            val updatedProject = state.project?.copy(
                sections = state.project.sections.filterNot { it.id == sectionId }
            )
            state.copy(
                project = updatedProject,
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
}

private fun Throwable.toValidationMessage(): String {
    val sectionNameError = (this as? SectionNameException)?.error ?: return message ?: "Invalid section name"
    return when (sectionNameError) {
        SectionNameError.TooShort -> "Section name must be at least 2 characters"
        SectionNameError.TooLong -> "Section name must be at most 50 characters"
    }
}
