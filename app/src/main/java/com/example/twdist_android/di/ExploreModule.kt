package com.example.twdist_android.di

import com.example.twdist_android.core.data.local.TWDistDatabase
import com.example.twdist_android.features.explore.data.remote.ExploreApi
import com.example.twdist_android.features.explore.data.repository.ProjectRepositoryImpl
import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import com.example.twdist_android.features.explore.application.usecases.ChangeProjectFavoriteUseCase
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
    fun provideProjectRepository(
        api: ExploreApi,
        db: TWDistDatabase
    ): ProjectRepository = ProjectRepositoryImpl(api, db)

    @Provides
    @Singleton
    fun provideGetProjectsUseCase(repository: ProjectRepository): GetProjectsUseCase =
        GetProjectsUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateProjectUseCase(repository: ProjectRepository): CreateProjectUseCase =
        CreateProjectUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteProjectUseCase(repository: ProjectRepository): DeleteProjectUseCase =
        DeleteProjectUseCase(repository)

    @Provides
    @Singleton
    fun provideChangeProjectFavoriteUseCase(repository: ProjectRepository): ChangeProjectFavoriteUseCase =
        ChangeProjectFavoriteUseCase(repository)
}
