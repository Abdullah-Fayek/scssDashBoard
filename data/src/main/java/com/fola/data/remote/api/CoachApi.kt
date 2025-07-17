package com.fola.data.remote.api

import com.fola.data.remote.dto.CoachDTO
import com.fola.data.remote.dto.CoachReviewDTO
import com.fola.domain.model.Coach
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface CoachApi {
    @GET("/api/Coachs")
    suspend fun getAllCoaches(): Response<List<CoachDTO>>

    @GET("/api/Coachs/{id}")
    suspend fun getCoach(
        @Path("id") id: Int,
        //       @Header("Authorization") token: String
    ): Response<CoachDTO>

    @Multipart
    @POST("/api/Coachs")
    suspend fun addCoach(
        @Part("FirstName") firstName: RequestBody,
        @Part("LastName") lastName: RequestBody,
        @Part("Specialty") specialty: RequestBody,
        @Part("Bio") bio: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<Unit>


    @Multipart
    @PUT("/api/Coachs/{id}")
    suspend fun updateCoach(
        @Path("id") id: Int,
        @Part("FirstName") firstName: RequestBody,
        @Part("LastName") lastName: RequestBody,
        @Part("Specialty") specialty: RequestBody,
        @Part("Bio") bio: RequestBody,
        @Part("Image") image: RequestBody
    ): Response<Unit>



    @PUT("/api/Coachs/{id}/toggle-status")
    suspend fun toggleState(
        @Path("id") id: Int,
    ): Response<Unit>

    @GET("/api/CoachReviews/Get-CoachReviews/{coachid}")
    suspend fun getCoachReview(
        @Path("coachid") id: Int
    ): Response<List<CoachReviewDTO>>

    @GET("/api/Appointments/appointmsOfcoach/{coachid}")
    suspend fun getCoachAppointment(
        @Path("id") id: Int
    )

    @POST("/api/ServiceCoach/Assign-Coach-To-Service")
    suspend fun assignCoachToService(
        @Body body: Map<String, Int>
    ): Response<Unit>

    @DELETE("/api/ServiceCoach/Assign-Coach-To-Service")
    suspend fun removeCoachFromService(@Body body: Map<String, Int>): Response<Unit>
}