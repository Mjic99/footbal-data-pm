package com.example.footballdatainocentemontemayor.models.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Team (
    @PrimaryKey() val id : Int,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "venue") val venue: String
)