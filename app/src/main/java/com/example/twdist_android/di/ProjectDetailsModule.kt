package com.example.twdist_android.di

import com.example.twdist_android.features.explore.data.remote.ExploreApi
import com.example.twdist_android.features.explore.domain.store.SectionStateStore
import com.example.twdist_android.features.projectdetails.application.usecases.GetProjectByIdUseCase
import com.example.twdist_android.features.projectdetails.data.repository.ProjectDetailsRepositoryImpl
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
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
        api: ExploreApi
    ): ProjectDetailsRepository =
        ProjectDetailsRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideGetProjectByIdUseCase(
        repository: ProjectDetailsRepository,
        sectionStateStore: SectionStateStore
    ): GetProjectByIdUseCase =
        GetProjectByIdUseCase(repository, sectionStateStore)
}
