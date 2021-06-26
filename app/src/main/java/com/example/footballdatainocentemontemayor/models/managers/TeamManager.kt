package com.example.footballdatainocentemontemayor.models.managers

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.footballdatainocentemontemayor.models.beans.Team
import com.example.footballdatainocentemontemayor.models.beans.TeamResponse
import com.example.footballdatainocentemontemayor.models.dao.TeamsService
import com.example.footballdatainocentemontemayor.models.persistence.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

interface OnGetTeamsDone {
    fun onTeamSuccess(newTeams : ArrayList<Team>)
    fun onTeamError(msg : String)
}

class TeamManager {
    companion object {
        private var instance : TeamManager? = null

        fun getInstance() : TeamManager {
            if (instance == null){
                instance = TeamManager()
            }
            return instance!!
        }
    }

    fun getTeams(callback : OnGetTeamsDone, context: Context, competitionId : Int)  {

        // Si es la primera vez obteniendo los datos
        if (isFirstTimeGetTeams(context)) {
            val retrofit = ConnectionManager.getInstance().getRetrofit()

            // Se comunica al API
            val teamsService = retrofit.create<TeamsService>()
            teamsService.getTeams(competitionId).enqueue(object : Callback<TeamResponse> {
                override fun onResponse(
                    call: Call<TeamResponse>,
                    response: Response<TeamResponse>
                ) {
                    if (response.body() != null) {
                        // Guardamos los equipos usando Room
                        saveTeamsRoom(response.body()!!.teams, context,competitionId, { products : ArrayList<Team> ->
                            val edit = context.getSharedPreferences(
                                "TEAMS_DATA", Context.MODE_PRIVATE).edit()
                            edit.putBoolean("IS_FIRST_TIME_TEAMS", false)
                            edit.commit()
                            callback.onTeamSuccess(response.body()!!.teams)
                        })
                    } else {
                        callback.onTeamError("Error al obtener equipos")
                    }
                }

                override fun onFailure(call: Call<TeamResponse>, t: Throwable) {
                    callback.onTeamError(t.message!!)
                }
            })
        //Si no es la primera vez, se sacan los equipos de SQLite
        } else {
            getTeamsRoom(context, {teams : ArrayList<Team> ->
                callback.onTeamSuccess(teams)})
        }
    }

    private fun getTeamsRoom(context: Context, callback : (ArrayList<Team>) -> Unit) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "FOOTBALLDATA_DB").fallbackToDestructiveMigration().build()

        Thread {
            val teamDAO = db.teamsDAO()

            val teamList = java.util.ArrayList<Team>()
            // Se obtienen todos los Equipos ganados en SQLite
            teamDAO.findAll().forEach{p: com.example.footballdatainocentemontemayor.models.persistence.entities.Team ->
                teamList.add(
                    Team(p.name,p.venue)
                )}
            callback(teamList)
        }.start()

    }

    // Obtener los equipos de una competición por su ID
     fun getTeamsByCompetitionId(competitionId: Int, context: Context,   callback : (ArrayList<Team>) -> Unit){
        val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "FOOTBALLDATA_DB").fallbackToDestructiveMigration().build()

        Thread {
            val teamDAO = db.teamsDAO()

            // Se usa el Query de findByCompetition para obtener todos los equipos según el ID requerido
            val teamList = java.util.ArrayList<Team>()
            teamDAO.findByCompetition(competitionId).forEach{p: com.example.footballdatainocentemontemayor.models.persistence.entities.Team ->
                teamList.add(
                        Team(p.name,p.venue)
                )}
            callback(teamList)
        }.start()
    }

    // Si es la primera vez obteniendo los equipos
    private fun isFirstTimeGetTeams(context: Context): Boolean {
        return context.getSharedPreferences("TEAMS_DATA",
            Context.MODE_PRIVATE).getBoolean("IS_FIRST_TIME_TEAMS", true)
    }

    // Guardar los equipos con Room
    private fun saveTeamsRoom(teams: ArrayList<Team>,
                              context : Context,
                              competitionId: Int,
                              callback : (products : ArrayList<Team>) -> Unit) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "FOOTBALLDATA_DB"
        ).fallbackToDestructiveMigration().build()

        // Se insertan los equipos
        Thread {
            val productDAO = db.teamsDAO()
            productDAO.insertTeams(teams, competitionId)
            callback(teams)
        }.start()
    }
}