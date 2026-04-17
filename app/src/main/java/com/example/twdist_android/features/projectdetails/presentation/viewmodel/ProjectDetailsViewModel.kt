package com.example.twdist_android.features.projectdetails.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.features.projectdetails.application.usecases.project.DeleteProjectUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.project.GetProjectByIdUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.project.UpdateProjectNameUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.section.CreateSectionUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.section.DeleteSectionUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.section.UpdateSectionNameUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.task.CompleteTaskUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.task.CreateTaskUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.task.DeleteTaskUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.task.GetTasksBySectionUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.task.UpdateTaskUseCase
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.Task
import com.example.twdist_android.features.projectdetails.domain.model.TaskName
import com.example.twdist_android.features.projectdetails.presentation.viewmodel.events.ProjectDetailsMutationContext
import com.example.twdist_android.features.projectdetails.presentation.viewmodel.events.ProjectEventDelegate
import com.example.twdist_android.features.projectdetails.presentation.viewmodel.events.SectionEventDelegate
import com.example.twdist_android.features.projectdetails.presentation.viewmodel.events.TaskEventDelegate
import com.example.twdist_android.features.projectdetails.presentation.event.ProjectEvent
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent
import com.example.twdist_android.features.projectdetails.presentation.event.TaskEvent
import com.example.twdist_android.features.projectdetails.presentation.mapper.toDetailsUi
import com.example.twdist_android.features.projectdetails.presentation.model.SectionUi
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUiState
import com.example.twdist_android.features.projectdetails.presentation.model.TaskUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val updateProjectNameUseCase: UpdateProjectNameUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val updateSectionNameUseCase: UpdateSectionNameUseCase,
    private val deleteSectionUseCase: DeleteSectionUseCase,
    private val createSectionUseCase: CreateSectionUseCase,
    private val getTasksBySectionUseCase: GetTasksBySectionUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel(), ProjectDetailsMutationContext {

    private var projectId: Long = -1L

    private val _uiState = MutableStateFlow(ProjectDetailsUiState())
    val uiState: StateFlow<ProjectDetailsUiState> = _uiState
    private val projectEventDelegate = ProjectEventDelegate(this)
    private val sectionEventDelegate = SectionEventDelegate(this)
    private val taskEventDelegate = TaskEventDelegate(this)

    override val state: ProjectDetailsUiState
        get() = _uiState.value

    override fun updateState(transform: (ProjectDetailsUiState) -> ProjectDetailsUiState) {
        _uiState.update(transform)
    }

    override fun launch(block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }

    override suspend fun updateProjectName(projectId: Long, name: ProjectName): Result<Unit> {
        return updateProjectNameUseCase(projectId, name)
    }

    override suspend fun deleteProject(projectId: Long): Result<Unit> {
        return deleteProjectUseCase(projectId)
    }

    override suspend fun updateSectionName(sectionId: Long, sectionName: SectionName): Result<Section> {
        return updateSectionNameUseCase(sectionId, sectionName)
    }

    override suspend fun deleteSection(sectionId: Long): Result<Unit> {
        return deleteSectionUseCase(sectionId)
    }

    override suspend fun createSection(projectId: Long, sectionName: SectionName): Result<Section> {
        return createSectionUseCase(projectId, sectionName)
    }

    override suspend fun createTask(projectId: Long, sectionId: Long, name: TaskName): Result<Task> {
        return createTaskUseCase(projectId, sectionId, name)
    }

    override suspend fun updateTask(projectId: Long, sectionId: Long, taskId: Long, name: TaskName): Result<Task> {
        return updateTaskUseCase(projectId, sectionId, taskId, name)
    }

    override suspend fun completeTask(
        projectId: Long,
        sectionId: Long,
        taskId: Long,
        completedDate: LocalDate?
    ): Result<Task> {
        return completeTaskUseCase(projectId, sectionId, taskId, completedDate)
    }

    override suspend fun deleteTask(projectId: Long, sectionId: Long, taskId: Long): Result<Unit> {
        return deleteTaskUseCase(projectId, sectionId, taskId)
    }

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
                            sectionActionError = null,
                            taskActionError = null,
                            sectionItems = aggregate.sections.map { section ->
                                SectionUi(
                                    id = section.id,
                                    name = section.name.asString(),
                                    taskIds = section.taskIds
                                )
                            },
                            tasksById = emptyMap()
                        )
                    }
                    loadTasksForSections()
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

    fun onSectionEvent(event: SectionEvent) {
        sectionEventDelegate.onEvent(event)
    }

    fun onTaskEvent(event: TaskEvent) {
        taskEventDelegate.onEvent(event)
    }

    fun onProjectEvent(event: ProjectEvent) {
        projectEventDelegate.onEvent(event)
    }

    private fun loadTasksForSections() {
        val currentProject = _uiState.value.project ?: return
        _uiState.value.sectionItems.forEach { section ->
            viewModelScope.launch {
                getTasksBySectionUseCase(currentProject.id, section.id)
                    .onSuccess { tasks ->
                        _uiState.update { state ->
                            val mapped = tasks.map {
                                TaskUi(id = it.id, name = it.name, completed = it.completed)
                            }
                            val visibleTasks = mapped.filterNot { it.completed }
                            val nextTasksById = state.tasksById.toMutableMap().apply {
                                visibleTasks.forEach { put(it.id.toString(), it) }
                            }
                            val nextSections = state.sectionItems.map {
                                if (it.id == section.id) it.copy(taskIds = visibleTasks.map { t -> t.id.toString() }) else it
                            }
                            state.copy(tasksById = nextTasksById, sectionItems = nextSections)
                        }
                    }
                    .onFailure { error ->
                        _uiState.update {
                            it.copy(taskActionError = error.message ?: "Could not load tasks")
                        }
                    }
            }
        }
    }

}

