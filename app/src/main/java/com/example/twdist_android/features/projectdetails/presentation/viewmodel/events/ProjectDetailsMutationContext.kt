package com.example.twdist_android.features.projectdetails.presentation.viewmodel.events

import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.domain.model.Task
import com.example.twdist_android.features.projectdetails.domain.model.TaskName
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState
import java.time.LocalDate

interface ProjectDetailsMutationContext {
    val state: ProjectDetailsUiState

    fun updateState(transform: (ProjectDetailsUiState) -> ProjectDetailsUiState)

    fun launch(block: suspend () -> Unit)

    suspend fun updateProjectName(projectId: Long, name: ProjectName): Result<Unit>

    suspend fun deleteProject(projectId: Long): Result<Unit>

    suspend fun updateSectionName(sectionId: Long, sectionName: SectionName): Result<Section>

    suspend fun deleteSection(sectionId: Long): Result<Unit>

    suspend fun createSection(projectId: Long, sectionName: SectionName): Result<Section>

    suspend fun createTask(projectId: Long, sectionId: Long, name: TaskName): Result<Task>

    suspend fun updateTask(projectId: Long, sectionId: Long, taskId: Long, name: TaskName): Result<Task>

    suspend fun completeTask(
        projectId: Long,
        sectionId: Long,
        taskId: Long,
        completedDate: LocalDate?
    ): Result<Task>

    suspend fun deleteTask(projectId: Long, sectionId: Long, taskId: Long): Result<Unit>
}
