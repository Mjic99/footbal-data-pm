package com.example.footballdatainocentemontemayor.models.dao

import com.example.footballdatainocentemontemayor.models.beans.CompetitionResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface CompetitionsService {
    @Headers("X-Auth-Token: eca211cf004c44d5870c86ed6586f25e")
    @GET("competitions")
    fun getCompetitions() : Call<CompetitionResponse>
}