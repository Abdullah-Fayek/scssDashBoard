package com.fola.data.remote.api

import com.fola.data.remote.dto.ProfileDTO
import com.fola.data.remote.dto.TokenPair
import com.fola.data.remote.dto.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {
    @POST("/api/Auth/login")
    suspend fun logIn(@Body body: Map<String, String>): Response<UserDTO>

    @POST("/api/Auth/revoke-refresh-token")
    suspend fun logOut(@Body body: Map<String, String>): Response<Unit>

    @POST("/api/Auth/refresh")
    suspend fun refreshToken(
        @Body body: Map<String, String>,
    ): Response<TokenPair>


    @GET("/me")
    suspend fun getUser(): Response<ProfileDTO>


}

