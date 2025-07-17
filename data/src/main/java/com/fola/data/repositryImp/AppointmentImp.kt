package com.fola.data.repositryImp

import android.util.Log
import com.fola.data.remote.api.AppointmentApi
import com.fola.data.remote.dto.AppointmentDto
import com.fola.data.remote.retrofitBuilder.retrofit
import com.fola.domain.model.Appointment
import com.fola.domain.repo.AppointmentRepository

class AppointmentImp : AppointmentRepository {

    private val c = retrofit.create(AppointmentApi::class.java)
    private val tag = "Appointment Imp"


    override suspend fun addAppointment(a: Appointment): Result<Unit> {
        val result = c.addAppointment(
            appointment = a,
        )
        return if (result.isSuccessful) {
            Result.success(Unit)
        } else {
            Log.d(tag, result.body().toString())
            Result.failure(Exception(result.message().toString()))
        }
    }


    override suspend fun removeAppointment(a : Appointment): Result<Unit> {
        val id: Int = a.id ?: return Result.failure(Exception("id is null"))
        val result = c.deleteAppointment(
            id = id
        )
        return if (result.isSuccessful)
            Result.success(Unit)
        else {
            Log.d(tag, result.body().toString())
            Result.failure(Exception(result.message().toString()))
        }
    }

    override suspend fun getAllAppointments(): Result<List<Appointment>> {
        val r = c.getAllAppointments()
        return if (r.isSuccessful) {
            r.body()?.let {
                Result.success(it.map { appointment ->
                    appointment.toAppointment()
                })
            } ?: Result.success(emptyList())
        } else {
            val code = r.code()
            val error = r.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }

    override suspend fun getAppointment(id: Int): Result<Appointment> {
        val r = c.getAppointment(
            id = id
        )
        return if (r.isSuccessful) {
            val body = r.body()
            if (body != null)
                Result.success(body.toAppointment())
            else
                Result.failure(Exception("No appointment"))
        } else {
            val code = r.code()
            val error = r.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }

    override suspend fun editAppointment(a: Appointment): Result<String> {
        val id: Int = a.id ?: return Result.failure(Exception("id is null"))
        val r = c.editAppointment(
      //      token = token,
            id = id,
            appointment = a
        )
        return if (r.isSuccessful)
            Result.success("Edited successfully")
        else {
            val code = r.code()
            val error = r.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }


    private fun AppointmentDto.toAppointment(): Appointment {
        return Appointment(
            id = id,
            coachId = coachId,
            serviceId = serviceId,
            day = day,
            maxAttenderNum = maxAttenderNum,
            time = time,
            duration = duration,
            currentAttendance = currentAttenderNum
        )
    }
}
