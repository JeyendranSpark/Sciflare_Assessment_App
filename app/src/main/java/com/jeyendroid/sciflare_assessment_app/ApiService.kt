package com.jeyendroid.sciflare_assessment_app

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("jeyendran")
    suspend fun getAllUserData(): List<UserDataModel>
}