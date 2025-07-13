package com.fola.domain.repo

import com.fola.domain.model.Coach
import com.fola.domain.model.CoachReviews

interface CoachRepository {
    suspend fun getAllCoaches(): Result<List<Coach>>
    suspend fun addCoach(coach: Coach): Result<String>
    suspend fun editCoach(coach: Coach): Result<String>
    suspend fun assignCoachToService(cId: Int, sId: Int): Result<String>
    suspend fun removeCoachFromService(cId: Int, sId: Int): Result<String>
    suspend fun toggleCoachState(id: Int): Result<String>
    suspend fun getCoachReviews(id: Int): Result<List<CoachReviews>>
    suspend fun getCoach(id: Int): Result<Coach>
}