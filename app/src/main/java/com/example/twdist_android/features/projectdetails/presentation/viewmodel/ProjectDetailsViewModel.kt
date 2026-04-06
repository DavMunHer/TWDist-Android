package com.example.twdist_android.features.projectdetails.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.features.projectdetails.application.usecases.DeleteProjectUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.DeleteSectionUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.GetProjectByIdUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.UpdateProjectNameUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.UpdateSectionNameUseCase
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.domain.model.ProjectNameError
import com.example.twdist_android.features.projectdetails.domain.model.ProjectNameException
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.domain.model.SectionNameError
import com.example.twdist_android.features.projectdetails.domain.model.SectionNameException
import com.example.twdist_android.features.projectdetails.presentation.event.ProjectEvent
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent
import com.example.twdist_android.features.projectdetails.presentation.mapper.removingSection
import com.example.twdist_android.features.projectdetails.presentation.mapper.renamingSection
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
    private val updateProjectNameUseCase: UpdateProjectNameUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
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
                sectionActionError = null
            )
        }
    }

    private fun onSectionOptionsDismiss() {
        _uiState.update { it.copy(openSectionMenuForId = null) }
    }

    private fun onEditSectionClick(sectionId: Long) {
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

    private fun onEditSectionNameChange(value: String) {
        _uiState.update {
            it.copy(
                editingSectionName = value,
                sectionActionError = null
            )
        }
    }

    private fun onEditSectionDismiss() {
        _uiState.update {
            it.copy(
                editingSectionId = null,
                editingSectionName = "",
                sectionActionError = null
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
            val updatedProject = state.project?.renamingSection(editingSectionId, name.asString())
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
                sectionActionError = null
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
            val updatedProject = state.project?.removingSection(sectionId)
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

private fun Throwable.toProjectValidationMessage(): String {
    val projectNameError = (this as? ProjectNameException)?.error ?: return message ?: "Invalid project name"
    return when (projectNameError) {
        ProjectNameError.TooShort -> "Project name must be at least 2 characters"
        ProjectNameError.TooLong -> "Project name must be at most 50 characters"
    }
}

private fun Throwable.toProjectActionMessage(fallbackMessage: String): String {
    return if (this is ProjectNameException) {
        toProjectValidationMessage()
    } else {
        fallbackMessage
    }
}
