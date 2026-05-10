package com.example.twdist_android.di

import com.example.twdist_android.core.data.local.TWDistDatabase
import com.example.twdist_android.features.upcoming.application.usecases.CompleteUpcomingTaskUseCase
import com.example.twdist_android.features.upcoming.application.usecases.GetUpcomingTasksUseCase
import com.example.twdist_android.features.upcoming.application.usecases.RefreshUpcomingTasksUseCase
import com.example.twdist_android.features.upcoming.application.usecases.UndoCompleteUpcomingTaskUseCase
import com.example.twdist_android.features.upcoming.data.remote.UpcomingApi
import com.example.twdist_android.features.upcoming.data.repository.UpcomingRepositoryImpl
import com.example.twdist_android.features.upcoming.domain.repository.UpcomingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UpcomingModule {

    @Provides
    @Singleton
    fun provideUpcomingRepository(
        api: UpcomingApi,
        db: TWDistDatabase
    ): UpcomingRepository = UpcomingRepositoryImpl(api, db)

    @Provides
    @Singleton
    fun provideGetUpcomingTasksUseCase(repository: UpcomingRepository): GetUpcomingTasksUseCase =
        GetUpcomingTasksUseCase(repository)

    @Provides
    @Singleton
    fun provideRefreshUpcomingTasksUseCase(repository: UpcomingRepository): RefreshUpcomingTasksUseCase =
        RefreshUpcomingTasksUseCase(repository)

    @Provides
    @Singleton
    fun provideCompleteUpcomingTaskUseCase(repository: UpcomingRepository): CompleteUpcomingTaskUseCase =
        CompleteUpcomingTaskUseCase(repository)

    @Provides
    @Singleton
    fun provideUndoCompleteUpcomingTaskUseCase(repository: UpcomingRepository): UndoCompleteUpcomingTaskUseCase =
        UndoCompleteUpcomingTaskUseCase(repository)
}
