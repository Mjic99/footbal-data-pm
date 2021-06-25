package com.example.footballdatainocentemontemayor.models.managers

import android.content.Context
import android.os.Debug
import android.util.Log
import androidx.room.Room
import com.example.footballdatainocentemontemayor.models.beans.Competition
import com.example.footballdatainocentemontemayor.models.beans.CompetitionResponse
import com.example.footballdatainocentemontemayor.models.dao.CompetitionsService
import com.example.footballdatainocentemontemayor.models.persistence.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

interface OnGetCompetitionsDone {
    fun onSuccess(newCompetitions : ArrayList<Competition>)
    fun onError(msg : String)
}

class CompetitionManager {

    companion object {
        private var instance : CompetitionManager? = null

        fun getInstance() : CompetitionManager {
            if (instance == null){
                instance = CompetitionManager()
            }
            return instance!!
        }
    }

    fun getCompetitions(callback : OnGetCompetitionsDone, context: Context)  {

        if (!hasLocalCompetitions(context)) {
            val retrofit = ConnectionManager.getInstance().getRetrofit()

            val competitionsService = retrofit.create<CompetitionsService>()
            competitionsService.getCompetitions().enqueue(object : Callback<CompetitionResponse> {
                override fun onResponse(
                    call: Call<CompetitionResponse>,
                    response: Response<CompetitionResponse>
                ) {
                    if (response.body() != null) {
                        saveCompetitionsRoom(response.body()!!.competitions, context) { competitions ->
                            context.getSharedPreferences(
                                "FOOTBALL_DATA", Context.MODE_PRIVATE
                            ).edit().putBoolean("HAS_LOCAL_COMPETITIONS", true).apply()
                            callback.onSuccess(competitions)
                        }
                    } else {
                        callback.onError("Error al obtener competiciones")
                    }
                }

                override fun onFailure(call: Call<CompetitionResponse>, t: Throwable) {
                    callback.onError(t.message!!)
                }
            })
        } else {
            getCompetitionsRoom(context) { competitions ->
                callback.onSuccess(competitions)
            }
        }
    }

    fun hasLocalCompetitions(context: Context): Boolean {
        return context.getSharedPreferences("FOOTBALL_DATA",
            Context.MODE_PRIVATE).getBoolean("HAS_LOCAL_COMPETITIONS", false)
    }

    fun getCompetitionsRoom(context: Context, callback: (ArrayList<Competition>) -> Unit) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "FOOTBALLDATA_DB"
        ).fallbackToDestructiveMigration().build()

        Thread {
            val competitionsDAO = db.competitionsDAO()

            val competitionList = java.util.ArrayList<Competition>()
            competitionsDAO.findAll().forEach { c : com.example.footballdatainocentemontemayor.models.persistence.entities.Competition ->
                competitionList.add(Competition(
                    c.id,
                    c.name
                ))
            }
            callback(competitionList)
        }.start()
    }

    private fun saveCompetitionsRoom(competitions: ArrayList<Competition>,
                                     context : Context,
                                     callback : (competitions : ArrayList<Competition>) -> Unit) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "FOOTBALLDATA_DB"
        ).fallbackToDestructiveMigration().build()

        Thread {
            val competitionsDAO = db.competitionsDAO()
            competitionsDAO.insertCompetitions(competitions)
            callback(competitions)
        }.start()
    }
}