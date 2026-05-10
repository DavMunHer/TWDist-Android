package com.example.twdist_android.di

import android.content.Context
import androidx.room.Room
import com.example.twdist_android.core.data.local.TWDistDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Only the database is exposed in the DI graph. Repositories that need persistence take [TWDistDatabase]
     * and resolve DAOs via [TWDistDatabase.taskDao], etc., so DAOs stay an implementation detail.
     */
    @Provides
    @Singleton
    fun provideTWDistDatabase(@ApplicationContext context: Context): TWDistDatabase =
        Room.databaseBuilder(context, TWDistDatabase::class.java, "twdist.db")
            .fallbackToDestructiveMigration()
            .build()
}
