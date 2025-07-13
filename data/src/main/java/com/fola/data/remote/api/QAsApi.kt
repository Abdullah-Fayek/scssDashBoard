package com.fola.data.remote.api

import com.fola.data.remote.dto.QAsDTO
import com.fola.domain.model.QA
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface QAsApi {

    @GET("/api/QAs")
    suspend fun getAllQAs(): Response<List<QAsDTO>>

    @GET("/api/QAs/{id}")
    suspend fun getQAs(@Path("id") id: Int): Response<QAsDTO>


    @PUT("/api/QAs/{id}")
    suspend fun editQAs(@Path("id") id: Int, @Body qAs: QA): Response<Unit>

    @DELETE("/api/QAs/{id}")
    suspend fun deleteQAs(@Path("id") id: Int): Response<Unit>


}