package com.example.twdist_android.di

import com.example.twdist_android.core.data.local.TWDistDatabase
import com.example.twdist_android.features.today.application.usecases.CompleteTodayTaskUseCase
import com.example.twdist_android.features.today.application.usecases.GetTodayTasksUseCase
import com.example.twdist_android.features.today.application.usecases.RefreshTodayTasksUseCase
import com.example.twdist_android.features.today.application.usecases.UndoCompleteTodayTaskUseCase
import com.example.twdist_android.features.today.data.remote.TodayApi
import com.example.twdist_android.features.today.data.repository.TodayRepositoryImpl
import com.example.twdist_android.features.today.domain.repository.TodayRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TodayModule {

    @Provides
    @Singleton
    fun provideTodayRepository(
        api: TodayApi,
        db: TWDistDatabase
    ): TodayRepository = TodayRepositoryImpl(api, db)

    @Provides
    @Singleton
    fun provideGetTodayTasksUseCase(repository: TodayRepository): GetTodayTasksUseCase =
        GetTodayTasksUseCase(repository)

    @Provides
    @Singleton
    fun provideRefreshTodayTasksUseCase(repository: TodayRepository): RefreshTodayTasksUseCase =
        RefreshTodayTasksUseCase(repository)

    @Provides
    @Singleton
    fun provideCompleteTodayTaskUseCase(repository: TodayRepository): CompleteTodayTaskUseCase =
        CompleteTodayTaskUseCase(repository)

    @Provides
    @Singleton
    fun provideUndoCompleteTodayTaskUseCase(repository: TodayRepository): UndoCompleteTodayTaskUseCase =
        UndoCompleteTodayTaskUseCase(repository)
}
