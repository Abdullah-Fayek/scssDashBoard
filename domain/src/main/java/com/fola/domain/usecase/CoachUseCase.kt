package com.fola.domain.usecase

import com.fola.domain.model.Coach
import com.fola.domain.model.CoachReviews
import com.fola.domain.model.Service
import com.fola.domain.repo.CoachRepository
import com.fola.domain.repo.ServicesRepository

class CoachUseCase {

    private val coachRepository: CoachRepository
    private val services: ServicesRepository

    private val TAG = "CoachUseCase"


    constructor(
        coachRepository: CoachRepository,
        servicesRepository: ServicesRepository
    ) {
        this.coachRepository = coachRepository
        this.services = servicesRepository
    }


    suspend fun getAllCoaches(): Result<List<Coach>> {
        val r = coachRepository.getAllCoaches()
        if (r.isSuccess) {
            val coaches = r.getOrNull()
            return if (coaches != null) {
                Result.success(coaches)
            } else {
                Result.failure(Exception("No coaches found"))
            }
        } else {
            val error = r.exceptionOrNull()
            return if (error != null) {
                Result.failure(error)
            } else {
                Result.failure(Exception("Unknown error occurred"))
            }
        }

    }

    suspend fun addCoach(coach: Coach): Result<String> {
        return coachRepository.addCoach(coach)
    }


    suspend fun editCoach(coach: Coach): Result<String> {
        return coachRepository.editCoach(coach)
    }

    suspend fun assignCoachToService(cId: Int, sId: Int): Result<String> {
        return coachRepository.assignCoachToService(cId, sId)
    }

    suspend fun removeCoachFromService(cId: Int, sId: Int): Result<String> {
        return coachRepository.removeCoachFromService(cId, sId)
    }

    suspend fun removeCoach(id: Int): Result<String> {
        return coachRepository.toggleCoachState(id)
    }

    suspend fun getCoachReviews(id: Int): Result<List<CoachReviews>> {
        return coachRepository.getCoachReviews(id)
    }

    suspend fun getCoach(id: Int): Result<Coach> {
        return coachRepository.getCoach(id)
    }


    suspend fun getAllServices(): Result<List<Service>> {
        val r = services.getAllServices()
        if (r.isSuccess) {
            val body = r.getOrNull()
            return if (body != null)
                Result.success(body)
            else
                Result.failure(Exception("No services found"))
        } else {
            val error = r.exceptionOrNull()
            return if (error != null)
                Result.failure(error)
            else
                Result.failure(Exception("cannot get services right now"))
        }
    }


}