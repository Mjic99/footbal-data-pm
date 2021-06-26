package com.example.footballdatainocentemontemayor.models.managers

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.footballdatainocentemontemayor.models.beans.Competition
import com.example.footballdatainocentemontemayor.models.beans.CompetitionResponse
import com.example.footballdatainocentemontemayor.models.beans.Team
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

class CompetitionManager : OnGetTeamsDone {

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
            // Si no existe data guardada localmente
            val retrofit = ConnectionManager.getInstance().getRetrofit()

            val competitionsService = retrofit.create<CompetitionsService>()
            // Se llama al listado de competiciones de la api
            competitionsService.getCompetitions().enqueue(object : Callback<CompetitionResponse> {
                override fun onResponse(
                    call: Call<CompetitionResponse>,
                    response: Response<CompetitionResponse>
                ) {
                    if (response.body() != null) {
                        // Se toman 3 competiciones del arreglo para no exceder el límite de llamadas al api
                        val listaCompeticiones = response.body()!!.competitions.take(3)
                        // Se guardan las competiciones en SQLite
                        saveCompetitionsRoom(listaCompeticiones as ArrayList<Competition>, context) { competitions ->
                            // Se guarda en shared preferences un booleano que nos indica que hay data local
                            context.getSharedPreferences(
                                "FOOTBALL_DATA", Context.MODE_PRIVATE
                            ).edit().putBoolean("HAS_LOCAL_COMPETITIONS", true).apply()
                            // Por cada competición se llamará a su respectivo endpoint de listado de equipos y resultados
                            // para que toda esa data se guarde en SQLite
                            competitions.forEach{ c : Competition ->
                                TeamManager.getInstance().getTeams(this@CompetitionManager, context, c.id)
                                StandingsManager.getInstance().persistCompetitionStandings(c.id, context)
                            }
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
            // Si ya hay data local se extraen las competiciones de SQLite
            getCompetitionsRoom(context) { competitions ->
                callback.onSuccess(competitions)
            }
        }
    }

    // Chequea en shared preferences el valor que indica que ya existe data en SQLite
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
            // Extrae el listado de competiciones de room y las devuelve como el objeto bean
            val competitionList = java.util.ArrayList<Competition>()
            competitionsDAO.findAll().forEach { c : com.example.footballdatainocentemontemayor.models.persistence.entities.Competition ->
                competitionList.add(Competition(
                    c.id,
                    c.name,
                    c.numberOfAvailableSeasons
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
        // Llama a la transaccion para insertar el listado de competiciones en Room
        Thread {
            val competitionsDAO = db.competitionsDAO()
            competitionsDAO.insertCompetitions(competitions)
            callback(competitions)
        }.start()
    }

    override fun onTeamSuccess(newTeams: ArrayList<Team>) {
        Log.i("CompetitionManager", "Llamada de equipos")
    }

    override fun onTeamError(msg: String) {
        Log.i("CompetitionManager", "Error al llamar equipos")
    }
}