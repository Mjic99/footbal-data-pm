package com.example.footballdatainocentemontemayor.models.managers

import android.content.Context
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

        if (isFirstTimeGetTeams(context)) {
            val retrofit = ConnectionManager.getInstance().getRetrofit()

            val teamsService = retrofit.create<TeamsService>()
            teamsService.getTeams(competitionId).enqueue(object : Callback<TeamResponse> {
                override fun onResponse(
                    call: Call<TeamResponse>,
                    response: Response<TeamResponse>
                ) {
                    if (response.body() != null) {
                        saveTeamsRoom(response.body()!!.teams, context, { products : ArrayList<Team> ->
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
            teamDAO.findAll().forEach{p: com.example.footballdatainocentemontemayor.models.persistence.entities.Team ->
                teamList.add(
                    Team(p.id,p.name,p.venue)
                )}
            callback(teamList)
        }.start()

    }

    private fun isFirstTimeGetTeams(context: Context): Boolean {
        return context.getSharedPreferences("TEAMS_DATA",
            Context.MODE_PRIVATE).getBoolean("IS_FIRST_TIME_TEAMS", true)
    }

    private fun saveTeamsRoom(teams: ArrayList<Team>,
                              context : Context,
                              callback : (products : ArrayList<Team>) -> Unit) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "FOOTBALLDATA_DB"
        ).fallbackToDestructiveMigration().build()

        Thread {
            val productDAO = db.teamsDAO()
            teams.forEach { p : Team ->
                productDAO.insert(com.example.footballdatainocentemontemayor.models.persistence.entities.Team(
                    p.id,
                    p.name,
                    p.venue,
                ))
            }
            callback(teams)
        }.start()
    }
}