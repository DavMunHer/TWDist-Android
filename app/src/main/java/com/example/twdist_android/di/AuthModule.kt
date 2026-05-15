package com.example.twdist_android.di

import com.example.twdist_android.core.network.CookieJarImpl
import com.example.twdist_android.features.auth.application.usecases.LoginUseCase
import com.example.twdist_android.features.auth.application.usecases.RefreshSessionUseCase
import com.example.twdist_android.features.auth.application.usecases.RegisterUseCase
import com.example.twdist_android.features.auth.application.usecases.RestoreSessionUseCase
import com.example.twdist_android.features.auth.data.remote.AuthApi
import com.example.twdist_android.features.auth.data.repository.AuthRepositoryImpl
import com.example.twdist_android.features.auth.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        json: Json,
        cookieJarImpl: CookieJarImpl
    ): AuthRepository {
        return AuthRepositoryImpl(authApi, json, cookieJarImpl)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase =
        LoginUseCase(authRepository)

    @Provides
    @Singleton
    fun provideRegisterUseCase(authRepository: AuthRepository): RegisterUseCase =
        RegisterUseCase(authRepository)

    @Provides
    @Singleton
    fun provideRestoreSessionUseCase(
        authRepository: AuthRepository,
        authSessionManager: com.example.twdist_android.features.auth.domain.session.AuthSessionManager
    ): RestoreSessionUseCase = RestoreSessionUseCase(authRepository, authSessionManager)

    @Provides
    @Singleton
    fun provideRefreshSessionUseCase(authRepository: AuthRepository): RefreshSessionUseCase =
        RefreshSessionUseCase(authRepository)
}
