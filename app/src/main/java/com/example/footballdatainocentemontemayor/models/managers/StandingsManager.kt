package com.example.footballdatainocentemontemayor.models.managers

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.footballdatainocentemontemayor.models.beans.*
import com.example.footballdatainocentemontemayor.models.dao.CompetitionsService
import com.example.footballdatainocentemontemayor.models.persistence.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class StandingsManager {

    companion object {
        private var instance : StandingsManager? = null

        fun getInstance() : StandingsManager {
            if (instance == null){
                instance = StandingsManager()
            }
            return instance!!
        }
    }

    fun persistCompetitionStandings(competitionId: Int, context: Context) {
        val retrofit = ConnectionManager.getInstance().getRetrofit()
        // Se llama al endpoint que devuelve los resultados de una competicion
        retrofit.create<CompetitionsService>().getStandings(competitionId).enqueue(object : Callback<StandingsResponse> {
            override fun onResponse(
                call: Call<StandingsResponse>,
                response: Response<StandingsResponse>
            ) {
                // Se extrae la data deseada del JSON devuelto y se pasa al formato de objeto deseado (PlainStanding)
                val standings = response.body()!!.standings[0].table.map { s -> PlainStanding(s.position, s.team.name, s.points) }
                // Se guarda lo obtenido en SQLite
                saveStandingsRoom(standings as ArrayList<PlainStanding>, competitionId, context)
            }

            override fun onFailure(call: Call<StandingsResponse>, t: Throwable) {
                Log.i("awa", "Error al llamar standings")
            }
        })
    }

    // Devuelve los resultados de una competición almacenados en SQLite
    fun getCompetitionStandingsRoom(competitionId: Int, context: Context, callback: (ArrayList<PlainStanding>) -> Unit) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "FOOTBALLDATA_DB"
        ).fallbackToDestructiveMigration().build()

        // Se crea un thread que llamará al callback cuando se consiga la data de SQLite
        Thread {
            val standingsDAO = db.standingsDAO()

            val standingList = java.util.ArrayList<PlainStanding>()
            standingsDAO.findCompetitionStandings(competitionId).forEach { s ->
                standingList.add(
                    PlainStanding(
                        s.position,
                        s.name,
                        s.score
                    )
                )
            }
            callback(standingList)
        }.start()
    }

    private fun saveStandingsRoom(standings: ArrayList<PlainStanding>,
                                  competitionId: Int,
                                  context : Context) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "FOOTBALLDATA_DB"
        ).fallbackToDestructiveMigration().build()

        Thread {
            val standingsDAO = db.standingsDAO()
            // Guarda los resultados de la competición en SQLite usando una transacción
            standingsDAO.insertStandings(competitionId, standings)
        }.start()
    }
}