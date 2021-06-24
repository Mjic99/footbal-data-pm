package com.example.footballdatainocentemontemayor.models.managers

import com.example.footballdatainocentemontemayor.models.beans.Competition
import com.example.footballdatainocentemontemayor.models.beans.CompetitionResponse
import com.example.footballdatainocentemontemayor.models.dao.CompetitionsService
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

    fun getCompetitions(callback : OnGetCompetitionsDone)  {

        val retrofit = ConnectionManager.getInstance().getRetrofit()

        val competitionsService = retrofit.create<CompetitionsService>()
        competitionsService.getCompetitions().enqueue(object : Callback<CompetitionResponse> {
            override fun onResponse(
                call: Call<CompetitionResponse>,
                response: Response<CompetitionResponse>
            ) {
                if (response.body() != null) {
                    callback.onSuccess(response.body()!!.competitions)
                } else {
                    callback.onError("Error al obtener competiciones")
                }
            }

            override fun onFailure(call: Call<CompetitionResponse>, t: Throwable) {
                callback.onError(t.message!!)
            }
        })
    }
}