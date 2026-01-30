package com.example.twdist_android.di

import com.example.twdist_android.features.explore.data.remote.ExploreApi
import com.example.twdist_android.features.explore.data.repository.ExploreRepositoryImpl
import com.example.twdist_android.features.explore.domain.repository.ExploreRepository
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
    fun provideExploreRepository(api: ExploreApi): ExploreRepository =
        ExploreRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideGetProjectsUseCase(repository: ExploreRepository): GetProjectsUseCase =
        GetProjectsUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateProjectUseCase(repository: ExploreRepository): CreateProjectUseCase =
        CreateProjectUseCase(repository)
}
