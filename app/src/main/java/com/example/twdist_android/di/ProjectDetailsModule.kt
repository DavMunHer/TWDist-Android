package com.example.twdist_android.di

import com.example.twdist_android.features.projectdetails.data.remote.ProjectDetailsApi
import com.example.twdist_android.features.projectdetails.domain.store.SectionStateStore
import com.example.twdist_android.features.projectdetails.data.store.inmemory.InMemorySectionStore
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
        api: ProjectDetailsApi
    ): ProjectDetailsRepository =
        ProjectDetailsRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideSectionRepository(
        api: ProjectDetailsApi
    ): SectionRepository =
        SectionRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideGetProjectByIdUseCase(
        repository: ProjectDetailsRepository,
        sectionStateStore: SectionStateStore
    ): GetProjectByIdUseCase =
        GetProjectByIdUseCase(repository, sectionStateStore)

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
}
