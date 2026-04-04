package com.example.twdist_android.di

import com.example.twdist_android.features.explore.data.remote.ExploreApi
import com.example.twdist_android.features.explore.data.repository.ProjectRepositoryImpl
import com.example.twdist_android.features.explore.data.repository.SectionRepositoryImpl
import com.example.twdist_android.features.explore.data.store.inmemory.InMemoryProjectStore
import com.example.twdist_android.features.explore.data.store.inmemory.InMemorySectionStore
import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import com.example.twdist_android.features.explore.domain.repository.SectionRepository
import com.example.twdist_android.features.explore.domain.store.ProjectStateStore
import com.example.twdist_android.features.explore.domain.store.SectionStateStore
import com.example.twdist_android.features.explore.domain.usecases.CreateProjectUseCase
import com.example.twdist_android.features.explore.domain.usecases.GetProjectsUseCase
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
    fun provideSectionStateStore(): SectionStateStore = InMemorySectionStore()

    @Provides
    @Singleton
    fun provideProjectRepository(
        api: ExploreApi
    ): ProjectRepository =
        ProjectRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideSectionRepository(
        api: ExploreApi
    ): SectionRepository =
        SectionRepositoryImpl(api)

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
}
