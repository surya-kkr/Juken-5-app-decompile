package com.kkr.juken.core.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val vehicleName: String,
    val ecuModel: String,
    val firmwareVersion: String,
    val lastModified: Long,
    val isFavorite: Boolean = false
)
