package com.example.twdist_android.di

import com.example.twdist_android.features.auth.data.remote.AuthApi
import com.example.twdist_android.features.auth.data.repository.AuthRepositoryImpl
import com.example.twdist_android.features.auth.domain.repository.AuthRepository
import com.example.twdist_android.features.auth.domain.usecases.LoginUseCase
import com.example.twdist_android.features.auth.domain.usecases.RegisterUseCase
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
        json: Json
    ): AuthRepository {
        return AuthRepositoryImpl(authApi, json)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase =
        LoginUseCase(authRepository)

    @Provides
    @Singleton
    fun provideRegisterUseCase(authRepository: AuthRepository): RegisterUseCase =
        RegisterUseCase(authRepository)
}
