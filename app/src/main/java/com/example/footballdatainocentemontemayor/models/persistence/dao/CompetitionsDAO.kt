package com.example.footballdatainocentemontemayor.models.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.footballdatainocentemontemayor.models.persistence.entities.Competition

@Dao
abstract class CompetitionsDAO {
    @Query("SELECT * FROM Competition")
    abstract fun findAll() : List<Competition>

    @Insert
    abstract fun insert(competition: Competition)

    @Transaction
    open fun insertCompetitions(products: ArrayList<com.example.footballdatainocentemontemayor.models.beans.Competition>) {
        products.forEach { p : com.example.footballdatainocentemontemayor.models.beans.Competition ->
            insert(Competition(
                p.id,
                p.name
            ))
        }
    }
}