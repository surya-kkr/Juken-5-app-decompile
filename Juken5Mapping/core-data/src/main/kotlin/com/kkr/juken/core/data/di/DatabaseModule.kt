package com.kkr.juken.core.data.di

import android.content.Context
import androidx.room.Room
import com.kkr.juken.core.data.db.JukenDatabase
import com.kkr.juken.core.data.db.dao.MapDao
import com.kkr.juken.core.data.db.dao.ProjectDao
import com.kkr.juken.core.data.db.dao.WorkspaceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): JukenDatabase {
        return Room.databaseBuilder(
            context,
            JukenDatabase::class.java,
            "juken_mapping.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideProjectDao(db: JukenDatabase): ProjectDao = db.projectDao()

    @Provides
    fun provideMapDao(db: JukenDatabase): MapDao = db.mapDao()

    @Provides
    fun provideWorkspaceDao(db: JukenDatabase): WorkspaceDao = db.workspaceDao()
}
