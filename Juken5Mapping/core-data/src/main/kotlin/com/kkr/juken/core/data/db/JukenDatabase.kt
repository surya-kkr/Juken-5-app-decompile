package com.kkr.juken.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kkr.juken.core.data.db.dao.MapDao
import com.kkr.juken.core.data.db.dao.ProjectDao
import com.kkr.juken.core.data.db.dao.WorkspaceDao
import com.kkr.juken.core.data.db.entity.MapEntity
import com.kkr.juken.core.data.db.entity.ProjectEntity
import com.kkr.juken.core.data.db.entity.WorkspaceEntity

@Database(
    entities = [ProjectEntity::class, MapEntity::class, WorkspaceEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class JukenDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun mapDao(): MapDao
    abstract fun workspaceDao(): WorkspaceDao
}
