package com.example.footballdatainocentemontemayor.models.dao

import com.example.footballdatainocentemontemayor.models.beans.CompetitionResponse
import com.example.footballdatainocentemontemayor.models.beans.StandingsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface CompetitionsService {
    @Headers("X-Auth-Token: eca211cf004c44d5870c86ed6586f25e")
    @GET("competitions?plan=TIER_ONE")
    fun getCompetitions() : Call<CompetitionResponse>

    @Headers("X-Auth-Token: eca211cf004c44d5870c86ed6586f25e")
    @GET("competitions/{id}/standings")
    fun getStandings(@Path("id") id: Int) : Call<StandingsResponse>
}