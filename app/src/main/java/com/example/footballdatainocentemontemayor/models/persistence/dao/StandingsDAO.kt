package com.example.footballdatainocentemontemayor.models.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.footballdatainocentemontemayor.models.beans.PlainStanding
import com.example.footballdatainocentemontemayor.models.persistence.entities.Standing

@Dao
abstract class StandingsDAO {
    @Query("SELECT * FROM Standing WHERE competitionid = :id")
    abstract fun findCompetitionStandings(id: Int) : List<Standing>

    @Insert
    abstract fun insert(standing: Standing)

    @Transaction
    open fun insertStandings(competitionId: Int, standings: ArrayList<PlainStanding>) {
        standings.forEach { s : PlainStanding ->
            insert(
                Standing(
                    0,
                    s.position,
                    s.name,
                    s.score,
                    competitionId
                )
            )
        }
    }
}