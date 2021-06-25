package com.example.footballdatainocentemontemayor.models.dao

import com.example.footballdatainocentemontemayor.models.beans.TeamResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface TeamsService {
    @Headers("X-Auth-Token: eca211cf004c44d5870c86ed6586f25e")
    @GET("competitions/{id}/teams")
    fun getTeams(@Path("id") id: Int) : Call<TeamResponse>
}