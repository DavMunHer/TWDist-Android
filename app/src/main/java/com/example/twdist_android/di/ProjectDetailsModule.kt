package com.example.twdist_android.di

import com.example.twdist_android.core.data.local.dao.ProjectDao
import com.example.twdist_android.core.data.local.dao.SectionDao
import com.example.twdist_android.core.data.local.dao.TaskDao
import com.example.twdist_android.features.projectdetails.data.remote.ProjectDetailsApi
import com.example.twdist_android.features.projectdetails.data.repository.ProjectDetailsRepositoryImpl
import com.example.twdist_android.features.projectdetails.data.repository.SectionRepositoryImpl
import com.example.twdist_android.features.projectdetails.data.repository.TaskRepositoryImpl
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
import com.example.twdist_android.features.projectdetails.domain.repository.SectionRepository
import com.example.twdist_android.features.projectdetails.domain.repository.TaskRepository
import com.example.twdist_android.features.projectdetails.application.usecases.project.*
import com.example.twdist_android.features.projectdetails.application.usecases.section.*
import com.example.twdist_android.features.projectdetails.application.usecases.task.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProjectDetailsModule {

    @Provides
    @Singleton
    fun provideProjectDetailsRepository(
        api: ProjectDetailsApi,
        projectDao: ProjectDao,
        sectionDao: SectionDao
    ): ProjectDetailsRepository = ProjectDetailsRepositoryImpl(api, projectDao, sectionDao)

    @Provides
    @Singleton
    fun provideSectionRepository(
        api: ProjectDetailsApi,
        sectionDao: SectionDao,
        taskDao: TaskDao
    ): SectionRepository = SectionRepositoryImpl(api, sectionDao, taskDao)

    @Provides
    @Singleton
    fun provideTaskRepository(
        api: ProjectDetailsApi,
        taskDao: TaskDao
    ): TaskRepository = TaskRepositoryImpl(api, taskDao)

    @Provides
    @Singleton
    fun provideGetProjectByIdUseCase(
        repository: ProjectDetailsRepository
    ): GetProjectByIdUseCase = GetProjectByIdUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateProjectNameUseCase(
        repository: ProjectDetailsRepository
    ): UpdateProjectNameUseCase = UpdateProjectNameUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteProjectUseCase(
        repository: ProjectDetailsRepository
    ): DeleteProjectUseCase = DeleteProjectUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateSectionUseCase(
        repository: SectionRepository
    ): CreateSectionUseCase = CreateSectionUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSectionsByProjectIdUseCase(
        repository: SectionRepository
    ): GetSectionsByProjectIdUseCase = GetSectionsByProjectIdUseCase(repository)

    @Provides
    @Singleton
    fun provideAddTaskIdToSectionUseCase(
        repository: SectionRepository
    ): AddTaskIdToSectionUseCase = AddTaskIdToSectionUseCase(repository)

    @Provides
    @Singleton
    fun provideReplaceTaskIdInSectionUseCase(
        repository: SectionRepository
    ): ReplaceTaskIdInSectionUseCase = ReplaceTaskIdInSectionUseCase(repository)

    @Provides
    @Singleton
    fun provideRemoveTaskIdFromSectionUseCase(
        repository: SectionRepository
    ): RemoveTaskIdFromSectionUseCase = RemoveTaskIdFromSectionUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateSectionNameUseCase(
        repository: SectionRepository
    ): UpdateSectionNameUseCase = UpdateSectionNameUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteSectionUseCase(
        repository: SectionRepository
    ): DeleteSectionUseCase = DeleteSectionUseCase(repository)

    @Provides
    @Singleton
    fun provideGetTasksBySectionUseCase(
        repository: TaskRepository
    ): GetTasksBySectionUseCase = GetTasksBySectionUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateTaskUseCase(
        repository: TaskRepository
    ): CreateTaskUseCase = CreateTaskUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateTaskUseCase(
        repository: TaskRepository
    ): UpdateTaskUseCase = UpdateTaskUseCase(repository)

    @Provides
    @Singleton
    fun provideCompleteTaskUseCase(
        repository: TaskRepository
    ): CompleteTaskUseCase = CompleteTaskUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteTaskUseCase(
        repository: TaskRepository
    ): DeleteTaskUseCase = DeleteTaskUseCase(repository)
}
