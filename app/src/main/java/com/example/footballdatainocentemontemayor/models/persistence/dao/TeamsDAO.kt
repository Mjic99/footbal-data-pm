package com.example.footballdatainocentemontemayor.models.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.footballdatainocentemontemayor.models.persistence.entities.Team

@Dao
abstract class TeamsDAO {
    @Query("SELECT * FROM Team")
    abstract fun findAll() : List<Team>

    @Insert
    abstract fun insert(team: Team)

    @Transaction
    open fun insertTeams(products: ArrayList<com.example.footballdatainocentemontemayor.models.beans.Team>, competitionID : Int) {
        products.forEach { p : com.example.footballdatainocentemontemayor.models.beans.Team ->
            insert(Team(0,
                p.name,
                competitionID,
                p.venue
            ))
        }
    }
}