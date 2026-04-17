package com.example.twdist_android.features.projectdetails.presentation.viewmodel.events

import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.presentation.event.ProjectEvent
import com.example.twdist_android.features.projectdetails.presentation.viewmodel.toProjectActionMessage
import com.example.twdist_android.features.projectdetails.presentation.viewmodel.toProjectValidationMessage

class ProjectEventDelegate(
    private val context: ProjectDetailsMutationContext
) {
    fun onEvent(event: ProjectEvent) {
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
        context.updateState { it.copy(openProjectMenu = true, projectActionError = null) }
    }

    private fun onProjectMenuDismissed() {
        context.updateState { it.copy(openProjectMenu = false) }
    }

    private fun onEditProjectClicked() {
        val currentName = context.state.project?.name ?: return
        context.updateState {
            it.copy(
                openProjectMenu = false,
                isEditingProject = true,
                editingProjectName = currentName,
                projectActionError = null
            )
        }
    }

    private fun onEditProjectNameChanged(value: String) {
        context.updateState { it.copy(editingProjectName = value, projectActionError = null) }
    }

    private fun onEditProjectDismissed() {
        context.updateState {
            it.copy(
                isEditingProject = false,
                editingProjectName = "",
                projectActionError = null
            )
        }
    }

    private fun onEditProjectConfirmed() {
        val projectId = context.state.project?.id ?: return
        val name = ProjectName.create(context.state.editingProjectName)
            .getOrElse { throwable ->
                context.updateState { it.copy(projectActionError = throwable.toProjectValidationMessage()) }
                return
            }

        val previousProject = context.state.project
        context.updateState { state ->
            state.copy(
                project = state.project?.copy(name = name.asString()),
                isEditingProject = false,
                editingProjectName = "",
                projectActionError = null
            )
        }

        context.launch {
            context.updateProjectName(projectId, name)
                .onSuccess {
                    context.updateState { it.copy(projectActionError = null) }
                }
                .onFailure { error ->
                    context.updateState {
                        it.copy(
                            project = previousProject,
                            projectActionError = error.toProjectActionMessage("Could not update project")
                        )
                    }
                }
        }
    }

    private fun onDeleteProjectClicked() {
        context.updateState {
            it.copy(
                openProjectMenu = false,
                deleteConfirmProject = true,
                projectActionError = null
            )
        }
    }

    private fun onDeleteProjectDismissed() {
        context.updateState { it.copy(deleteConfirmProject = false) }
    }

    private fun onDeleteProjectConfirmed() {
        val projectId = context.state.project?.id ?: return
        context.updateState { it.copy(deleteConfirmProject = false, projectActionError = null) }

        context.launch {
            context.deleteProject(projectId)
                .onSuccess {
                    context.updateState {
                        it.copy(
                            project = null,
                            projectDeleted = true,
                            projectActionError = null
                        )
                    }
                }
                .onFailure { error ->
                    context.updateState {
                        it.copy(
                            projectActionError = error.toProjectActionMessage("Could not delete project")
                        )
                    }
                }
        }
    }

    private fun onProjectDeletedHandled() {
        context.updateState { it.copy(projectDeleted = false) }
    }
}
