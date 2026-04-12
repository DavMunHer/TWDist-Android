package com.example.twdist_android.di

import com.example.twdist_android.features.explore.data.remote.ExploreApi
import com.example.twdist_android.features.explore.data.repository.ProjectRepositoryImpl
import com.example.twdist_android.features.explore.data.store.inmemory.InMemoryProjectStore
import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import com.example.twdist_android.features.explore.domain.store.ProjectStateStore
import com.example.twdist_android.features.explore.application.usecases.CreateProjectUseCase
import com.example.twdist_android.features.explore.application.usecases.DeleteProjectUseCase
import com.example.twdist_android.features.explore.application.usecases.GetProjectsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExploreModule {

    @Provides
    @Singleton
    fun provideProjectStateStore(): ProjectStateStore = InMemoryProjectStore()

    @Provides
    @Singleton
    fun provideProjectRepository(
        api: ExploreApi
    ): ProjectRepository =
        ProjectRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideGetProjectsUseCase(
        repository: ProjectRepository,
        projectStateStore: ProjectStateStore
    ): GetProjectsUseCase =
        GetProjectsUseCase(repository, projectStateStore)

    @Provides
    @Singleton
    fun provideCreateProjectUseCase(
        repository: ProjectRepository,
        projectStateStore: ProjectStateStore
    ): CreateProjectUseCase =
        CreateProjectUseCase(repository, projectStateStore)

    @Provides
    @Singleton
    fun provideDeleteProjectUseCase(
        repository: ProjectRepository,
        projectStateStore: ProjectStateStore
    ): DeleteProjectUseCase =
        DeleteProjectUseCase(repository, projectStateStore)
}
