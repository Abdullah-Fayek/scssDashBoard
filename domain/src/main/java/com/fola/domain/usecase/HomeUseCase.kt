package com.fola.domain.usecase

import android.util.Log
import com.fola.domain.model._Appointment
import com.fola.domain.repo.AppointmentRepository
import com.fola.domain.repo.CoachRepository
import com.fola.domain.repo.ServicesRepository

class HomeUseCase {

    private val appointmentRepo: AppointmentRepository
    private val coachesRepo: CoachRepository
    private val servicesRepo: ServicesRepository
    private val tag : String = "HomeUseCase"


    constructor(
        appointmentRepository: AppointmentRepository,
        coachRepository: CoachRepository,
        servicesRepository: ServicesRepository
    ) {
        this.appointmentRepo = appointmentRepository
        this.coachesRepo = coachRepository
        this.servicesRepo = servicesRepository
    }

    suspend fun getAllAppointments(): Result<List<_Appointment>> {
        val coaches = coachesRepo.getAllCoaches()
        val appointments = appointmentRepo.getAllAppointments()
        val serviceState = servicesRepo.getAllServices()

        return if (appointments.isSuccess && coaches.isSuccess && serviceState.isSuccess) {
            val appointmentList = appointments.getOrNull() ?: emptyList()
            val coachList = coaches.getOrNull() ?: emptyList()
            val serviceList = serviceState.getOrNull() ?: emptyList()


            Log.d(tag, "Appointments: $appointmentList")
            Log.d(tag, "Coaches: $coachList")
            Log.d(tag, "Services: $serviceList")

            Result.success(
                appointmentList.map { appointment ->
                    _Appointment(
                        id = appointment.id ?: 0,
                        coachName = coachList.find { it.id == appointment.coachId }?.firstName.plus(
                            " "
                        )
                            .plus(coachList.find { it.id == appointment.coachId }?.lastName),
                        serviceName = serviceList.find { it.id == appointment.serviceId }?.name
                            ?: "Unknown Service",
                        day = appointment.day,
                        time = appointment.time,
                        duration = appointment.duration,
                        maxAttenderNum = appointment.maxAttenderNum,
                        currentAttendance = appointment.currentAttendance ?: 0,
                    )
                }
            )
        } else {
            Result.failure(Exception("Failed to fetch appointments, coaches, or services"))
        }

    }
}