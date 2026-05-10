package com.example.twdist_android.di

import android.content.Context
import androidx.room.Room
import com.example.twdist_android.core.data.local.TWDistDatabase
import com.example.twdist_android.core.data.local.dao.ProjectDao
import com.example.twdist_android.core.data.local.dao.SectionDao
import com.example.twdist_android.core.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTWDistDatabase(@ApplicationContext context: Context): TWDistDatabase =
        Room.databaseBuilder(context, TWDistDatabase::class.java, "twdist.db").build()

    @Provides
    fun provideProjectDao(db: TWDistDatabase): ProjectDao = db.projectDao()

    @Provides
    fun provideSectionDao(db: TWDistDatabase): SectionDao = db.sectionDao()

    @Provides
    fun provideTaskDao(db: TWDistDatabase): TaskDao = db.taskDao()
}
