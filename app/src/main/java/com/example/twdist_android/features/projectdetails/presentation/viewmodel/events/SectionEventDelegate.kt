package com.example.twdist_android.features.projectdetails.presentation.viewmodel.events

import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent
import com.example.twdist_android.features.projectdetails.presentation.model.SectionUi
import com.example.twdist_android.features.projectdetails.presentation.viewmodel.toValidationMessage

class SectionEventDelegate(
    private val context: ProjectDetailsMutationContext
) {
    fun onEvent(event: SectionEvent) {
        when (event) {
            SectionEvent.AddSectionClicked -> onAddSectionClick()
            is SectionEvent.CreateSectionNameChanged -> onCreateSectionNameChanged(event.name)
            SectionEvent.CreateSectionConfirmed -> onCreateSectionConfirmed()
            SectionEvent.CreateSectionDismissed -> onCreateSectionDismissed()
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

    private fun onSectionOptionsClick(sectionId: Long) {
        context.updateState {
            it.copy(
                openSectionMenuForId = sectionId,
                isCreatingSection = false,
                sectionActionError = null,
                taskActionError = null
            )
        }
    }

    private fun onSectionOptionsDismiss() {
        context.updateState { it.copy(openSectionMenuForId = null) }
    }

    private fun onEditSectionClick(sectionId: Long) {
        val section = context.state.sectionItems.firstOrNull { it.id == sectionId } ?: return
        context.updateState {
            it.copy(
                openSectionMenuForId = null,
                isCreatingSection = false,
                editingSectionId = sectionId,
                editingSectionName = section.name,
                sectionActionError = null,
                taskActionError = null
            )
        }
    }

    private fun onEditSectionNameChange(value: String) {
        context.updateState {
            it.copy(
                editingSectionName = value,
                sectionActionError = null,
                taskActionError = null
            )
        }
    }

    private fun onEditSectionDismiss() {
        context.updateState {
            it.copy(
                editingSectionId = null,
                editingSectionName = "",
                sectionActionError = null,
                taskActionError = null
            )
        }
    }

    private fun onSaveSectionEdit() {
        val editingSectionId = context.state.editingSectionId ?: return
        val name = SectionName.create(context.state.editingSectionName)
            .getOrElse { throwable ->
                context.updateState { it.copy(sectionActionError = throwable.toValidationMessage()) }
                return
            }
        val previousProject = context.state.project

        context.updateState { state ->
            val updatedSections = state.sectionItems.map { section ->
                if (section.id == editingSectionId) section.copy(name = name.asString()) else section
            }
            state.copy(
                project = state.project?.copy(sections = updatedSections.map { it.name }),
                sectionItems = updatedSections,
                editingSectionId = null,
                editingSectionName = "",
                sectionActionError = null
            )
        }

        context.launch {
            context.updateSectionName(editingSectionId, name)
                .onSuccess {
                    context.updateState { it.copy(sectionActionError = null) }
                }
                .onFailure { error ->
                    context.updateState {
                        it.copy(
                            project = previousProject,
                            sectionActionError = error.message ?: "Could not update section"
                        )
                    }
                }
        }
    }

    private fun onDeleteSectionClick(sectionId: Long) {
        context.updateState {
            it.copy(
                openSectionMenuForId = null,
                isCreatingSection = false,
                deleteConfirmSectionId = sectionId,
                sectionActionError = null,
                taskActionError = null
            )
        }
    }

    private fun onDeleteSectionDismiss() {
        context.updateState { it.copy(deleteConfirmSectionId = null) }
    }

    private fun onDeleteSectionConfirm() {
        val sectionId = context.state.deleteConfirmSectionId ?: return
        val previousProject = context.state.project
        context.updateState { state ->
            val updatedSections = state.sectionItems.filterNot { it.id == sectionId }
            state.copy(
                project = state.project?.copy(sections = updatedSections.map { it.name }),
                sectionItems = updatedSections,
                deleteConfirmSectionId = null,
                sectionActionError = null
            )
        }

        context.launch {
            context.deleteSection(sectionId)
                .onSuccess {
                    context.updateState { it.copy(sectionActionError = null) }
                }
                .onFailure { error ->
                    context.updateState {
                        it.copy(
                            project = previousProject,
                            sectionActionError = error.message ?: "Could not delete section"
                        )
                    }
                }
        }
    }

    private fun onAddSectionClick() {
        context.updateState {
            it.copy(
                isCreatingSection = true,
                creatingSectionName = "",
                sectionActionError = null,
                taskActionError = null
            )
        }
    }

    private fun onCreateSectionNameChanged(name: String) {
        context.updateState { it.copy(creatingSectionName = name, sectionActionError = null) }
    }

    private fun onCreateSectionDismissed() {
        context.updateState {
            it.copy(
                isCreatingSection = false,
                creatingSectionName = "",
                isSectionCreateLoading = false,
                sectionActionError = null
            )
        }
    }

    private fun onCreateSectionConfirmed() {
        val state = context.state
        val currentProjectId = state.project?.id ?: return
        val sectionName = SectionName.create(state.creatingSectionName)
            .getOrElse { throwable ->
                context.updateState { it.copy(sectionActionError = throwable.toValidationMessage()) }
                return
            }
        context.updateState { it.copy(isSectionCreateLoading = true, sectionActionError = null) }
        context.launch {
            context.createSection(currentProjectId, sectionName)
                .onSuccess { createdSection ->
                    context.updateState { current ->
                        val createdSectionUi = SectionUi(
                            id = createdSection.id,
                            name = createdSection.name.asString(),
                            taskIds = createdSection.taskIds
                        )
                        val nextSections = current.sectionItems + createdSectionUi
                        current.copy(
                            project = current.project?.copy(sections = nextSections.map { it.name }),
                            sectionItems = nextSections,
                            isCreatingSection = false,
                            creatingSectionName = "",
                            isSectionCreateLoading = false,
                            sectionActionError = null
                        )
                    }
                }
                .onFailure { error ->
                    context.updateState {
                        it.copy(
                            sectionActionError = error.message ?: "Could not create section",
                            isSectionCreateLoading = false
                        )
                    }
                }
        }
    }
}
