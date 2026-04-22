package com.example.twdist_android.di

import com.example.twdist_android.BuildConfig
import com.example.twdist_android.core.network.CookieJarImpl
import com.example.twdist_android.features.auth.data.remote.AuthApi
import com.example.twdist_android.features.explore.data.remote.ExploreApi
import com.example.twdist_android.features.projectdetails.data.remote.ProjectDetailsApi
import com.example.twdist_android.features.today.data.remote.TodayApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    @Provides
    @Singleton
    fun provideCookieJar(@dagger.hilt.android.qualifiers.ApplicationContext context: android.content.Context): okhttp3.CookieJar {
        return CookieJarImpl(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cookieJar: okhttp3.CookieJar): OkHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideExploreApi(retrofit: Retrofit): ExploreApi =
        retrofit.create(ExploreApi::class.java)

    @Provides
    @Singleton
    fun provideProjectDetailsApi(retrofit: Retrofit): ProjectDetailsApi =
        retrofit.create(ProjectDetailsApi::class.java)

    @Provides
    @Singleton
    fun provideTodayApi(retrofit: Retrofit): TodayApi =
        retrofit.create(TodayApi::class.java)
}
