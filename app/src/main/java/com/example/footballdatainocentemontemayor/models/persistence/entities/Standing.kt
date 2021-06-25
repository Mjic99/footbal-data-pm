package com.example.footballdatainocentemontemayor.models.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Standing (
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "position") val position : Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "score") val score: Int,
    @ColumnInfo(name = "competitionid") val competitionId: Int
)