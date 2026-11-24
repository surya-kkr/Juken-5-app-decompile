package com.kkr.juken.core.data.db.dao

import androidx.room.*
import com.kkr.juken.core.data.db.entity.MapEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MapDao {
    @Query("SELECT * FROM maps WHERE projectId = :projectId AND type = :type")
    fun getMap(projectId: String, type: String): Flow<MapEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMap(map: MapEntity)

    @Query("DELETE FROM maps WHERE projectId = :projectId")
    suspend fun deleteMapsForProject(projectId: String)
}
