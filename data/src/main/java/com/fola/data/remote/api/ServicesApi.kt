package com.fola.data.remote.api

import com.fola.data.remote.dto.ServiceDTO
import com.fola.data.remote.dto.ServiceReviewDTO
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ServicesApi {

    @Multipart
    @POST("/api/Services")
    suspend fun addService(
        @Part("Name") name: RequestBody,
        @Part("Price") price: RequestBody,
        @Part("Description") description: RequestBody
    ): Response<Unit>

    @GET("/api/Services/{id}")
    suspend fun getService(@Path("id") id: Int): Response<ServiceDTO>

    @GET("/api/Services")
    suspend fun getAllServices(): Response<List<ServiceDTO>>

    @PUT("/api/Services/{id}")
    suspend fun editService(
        @Path("id") id: Int,
        @Part("Name") name: RequestBody,
        @Part("Price") price: RequestBody,
        @Part("Description") description: RequestBody
    ): Response<Unit>

    @DELETE("/api/Services/{id}")
    suspend fun deleteService(@Path("id") id: Int): Response<Unit>

    @POST("/api/ServiceCoach/Assign-Coach-To-Service")
    suspend fun assignCoachToService(@Body body: Map<String, Int>): Response<Unit>


    @DELETE("/api/ServiceCoach/Assign-Coach-To-Service")
    suspend fun removeCoachFromService(@Body body: Map<String, Int>): Response<Unit>

    @GET("/api/ServiceReviews/Get-ServiceReviews/{serviceid}")
    suspend fun getServiceReviews(@Path("serviceid") serviceId: Int): Response<List<ServiceReviewDTO>>



}