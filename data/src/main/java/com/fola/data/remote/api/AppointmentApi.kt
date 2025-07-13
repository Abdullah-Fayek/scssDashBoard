package com.fola.data.remote.api

import com.fola.data.remote.dto.AppointmentDto
import com.fola.domain.model.Appointment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AppointmentApi {

    @POST("/api/Appointments")
    suspend fun addAppointment(
        @Body appointment: Appointment,
        //   @Header("Authorization") token: String
    ): Response<Unit>

    @GET("/api/Appointments")
    suspend fun getAllAppointments(
        // @Header("Authorization") token: String
    ): Response<List<AppointmentDto>>

    @GET("/api/Appointments/{id}")
    suspend fun getAppointment(
        // @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<AppointmentDto>

    @DELETE("/api/Appointments/{id}")
    suspend fun deleteAppointment(
        //@Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @PUT("/api/Appointments/{id}")
    suspend fun editAppointment(
        //@Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body appointment: Appointment
    ): Response<Unit>


}