package com.example.hw_3.retrofit

import com.example.hw_3.model.PersonDetailsGit
import com.example.hw_3.model.PersonGitHub
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitAPI {

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): List<PersonGitHub>

    @GET("users/{username}")
    suspend fun getUserDetails(
        @Path("username") name: String
    ): PersonDetailsGit
}