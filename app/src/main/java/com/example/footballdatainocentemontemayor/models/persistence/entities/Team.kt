package com.example.footballdatainocentemontemayor.models.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Team (
    @PrimaryKey(autoGenerate = true) val id : Long,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "competitionID") val competitionID : Int,
    @ColumnInfo(name = "venue") val venue: String
)