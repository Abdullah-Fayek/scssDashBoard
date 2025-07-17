package com.fola.domain.usecase

import android.util.Log
import com.fola.domain.model.QA
import com.fola.domain.model._Appointment
import com.fola.domain.repo.AppointmentRepository
import com.fola.domain.repo.CoachRepository
import com.fola.domain.repo.QAsRepository
import com.fola.domain.repo.ServicesRepository
import com.fola.domain.repo.UserRepository

class HomeUseCase {

    private val appointmentRepo: AppointmentRepository
    private val coachesRepo: CoachRepository
    private val servicesRepo: ServicesRepository
    private val qAsRepo: QAsRepository
  //  private val userRepo: UserRepository
    private val tag: String = "HomeUseCase"


    constructor(
        appointmentRepository: AppointmentRepository,
        coachRepository: CoachRepository,
        servicesRepository: ServicesRepository,
        qAsRepository: QAsRepository,
//        userRepository: UserRepository
    ) {
        this.appointmentRepo = appointmentRepository
        this.coachesRepo = coachRepository
        this.servicesRepo = servicesRepository
        this.qAsRepo = qAsRepository
  //      this.userRepo = userRepository
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


    suspend fun getUnansweredQA(): List<QA> {
        val result = qAsRepo.getAllQAs()
        Log.d(tag, "Unanswered QAs: ${result.isSuccess}")
        return if (result.isSuccess) {
            val qas = result.getOrNull() ?: emptyList()
            val unansweredQAs = qas.filter { it.answer.isNullOrEmpty() }
            unansweredQAs.ifEmpty {
                qas.ifEmpty {
                    emptyList()
                }
            }
        } else {
            emptyList()
        }
    }


    suspend fun answerQA(qa: QA): Result<String> {
        return if (qa.answer.isNullOrEmpty()) {
            Result.failure(Exception("Answer cannot be empty"))
        } else {
            val r = qAsRepo.editeQA(qa)
            if (r.isSuccess) {
                Result.success("QA answered successfully")
            } else {
                val error = r.exceptionOrNull()
                Result.failure(error ?: Exception("Failed to answer QA"))
            }
        }
    }

    suspend fun getCoachesNumber(): Result<Int> {
        val result = coachesRepo.getAllCoaches()
        return if (result.isSuccess) {
            val coaches = result.getOrNull() ?: emptyList()
            Result.success(coaches.size)
        } else {
            Result.failure(Exception("Failed to fetch coaches"))
        }
    }

    suspend fun getServicesNumber(): Result<Int> {
        val result = servicesRepo.getAllServices()
        return if (result.isSuccess) {
            val services = result.getOrNull() ?: emptyList()
            Result.success(services.size)
        } else {
            Result.failure(Exception("Failed to fetch services"))
        }
    }

    suspend fun getAppointmentsNumber(): Result<Int> {
        val result = appointmentRepo.getAllAppointments()
        return if (result.isSuccess) {
            val appointments = result.getOrNull() ?: emptyList()
            Result.success(appointments.size)
        } else {
            Result.failure(Exception("Failed to fetch appointments"))
        }
    }

//    suspend fun getMembersNumber(): Result<Int> {
//        val result = userRepo
//        return if (result.isSuccess) {
//            val users = result.getOrNull() ?: emptyList()
//            Result.success(users.size)
//        } else {
//            Result.failure(Exception("Failed to fetch members"))
//        }
//    }




}



