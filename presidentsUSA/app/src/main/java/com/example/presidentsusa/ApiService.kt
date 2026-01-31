package com.example.presidentsusa

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/presidents/presidents")
    suspend fun getPresidents(): List<President>

    @GET("/presidents/presidents/{PresidentNo}")
    suspend fun getOnePresident(
        @Path("PresidentNo") presidentNo: String
    ): Response<President>
}