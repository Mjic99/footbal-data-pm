package com.example.footballdatainocentemontemayor.models.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.footballdatainocentemontemayor.models.persistence.entities.Competition
import com.example.footballdatainocentemontemayor.models.persistence.dao.CompetitionsDAO

@Database(entities = arrayOf(Competition::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun competitionsDAO() : CompetitionsDAO
}