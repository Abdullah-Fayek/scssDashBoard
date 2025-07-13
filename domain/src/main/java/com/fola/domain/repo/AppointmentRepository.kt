package com.fola.domain.repo

import com.fola.domain.model.Appointment

interface AppointmentRepository {

    suspend fun getAppointment(id: Int): Result<Appointment>
    suspend fun getAllAppointments(): Result<List<Appointment>>
    suspend fun addAppointment(a: Appointment): Result<Unit>
    suspend fun editAppointment(a: Appointment): Result<String>

    suspend fun removeAppointment(a : Appointment): Result<Unit>
}