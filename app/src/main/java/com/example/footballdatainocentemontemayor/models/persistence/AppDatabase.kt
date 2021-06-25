package com.example.footballdatainocentemontemayor.models.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.footballdatainocentemontemayor.models.persistence.entities.Competition
import com.example.footballdatainocentemontemayor.models.persistence.dao.CompetitionsDAO
import com.example.footballdatainocentemontemayor.models.persistence.dao.TeamsDAO
import com.example.footballdatainocentemontemayor.models.persistence.entities.Team

@Database(entities = arrayOf(Competition::class, Team::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun competitionsDAO() : CompetitionsDAO
    abstract fun teamsDAO() : TeamsDAO
}