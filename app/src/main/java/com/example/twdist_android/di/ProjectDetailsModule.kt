package com.example.twdist_android.di

import com.example.twdist_android.features.projectdetails.data.remote.ProjectDetailsApi
import com.example.twdist_android.features.projectdetails.domain.store.SectionStateStore
import com.example.twdist_android.features.projectdetails.data.store.inmemory.InMemorySectionStore
import com.example.twdist_android.features.explore.domain.store.ProjectStateStore
import com.example.twdist_android.features.projectdetails.application.usecases.GetProjectByIdUseCase
import com.example.twdist_android.features.projectdetails.data.repository.ProjectDetailsRepositoryImpl
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
import com.example.twdist_android.features.projectdetails.data.repository.SectionRepositoryImpl
import com.example.twdist_android.features.projectdetails.domain.repository.SectionRepository
import com.example.twdist_android.features.projectdetails.application.usecases.*
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
    fun provideSectionStateStore(): SectionStateStore = InMemorySectionStore()

    @Provides
    @Singleton
    fun provideProjectDetailsRepository(
        api: ProjectDetailsApi,
        projectStateStore: ProjectStateStore
    ): ProjectDetailsRepository =
        ProjectDetailsRepositoryImpl(api, projectStateStore)

    @Provides
    @Singleton
    fun provideSectionRepository(
        api: ProjectDetailsApi,
        sectionStateStore: SectionStateStore
    ): SectionRepository =
        SectionRepositoryImpl(api, sectionStateStore)

    @Provides
    @Singleton
    fun provideGetProjectByIdUseCase(
        repository: ProjectDetailsRepository,
        sectionStateStore: SectionStateStore
    ): GetProjectByIdUseCase =
        GetProjectByIdUseCase(repository, sectionStateStore)

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
        repository: SectionRepository,
        sectionStateStore: SectionStateStore
    ): GetSectionsByProjectIdUseCase = GetSectionsByProjectIdUseCase(repository, sectionStateStore)

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
}
