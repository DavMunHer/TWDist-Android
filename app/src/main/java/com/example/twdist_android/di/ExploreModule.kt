package com.example.twdist_android.di

import com.example.twdist_android.features.explore.data.remote.ExploreApi
import com.example.twdist_android.features.explore.data.repository.ProjectRepositoryImpl
import com.example.twdist_android.features.explore.data.repository.SectionRepositoryImpl
import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import com.example.twdist_android.features.explore.domain.repository.SectionRepository
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
    fun provideProjectRepository(api: ExploreApi): ProjectRepository =
        ProjectRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideSectionRepository(api: ExploreApi): SectionRepository =
        SectionRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideGetProjectsUseCase(repository: ProjectRepository): GetProjectsUseCase =
        GetProjectsUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateProjectUseCase(repository: ProjectRepository): CreateProjectUseCase =
        CreateProjectUseCase(repository)
}
