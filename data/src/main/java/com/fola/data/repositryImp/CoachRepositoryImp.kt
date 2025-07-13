package com.fola.data.repositryImp

import com.fola.data.remote.api.CoachApi
import com.fola.data.remote.dto.CoachDTO
import com.fola.data.remote.dto.CoachReviewDTO
import com.fola.data.remote.retrofitBuilder.retrofit
import com.fola.domain.model.Coach
import com.fola.domain.model.CoachReviews
import com.fola.domain.repo.CoachRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class CoachRepositoryImp : CoachRepository {

    private val c = retrofit.create(CoachApi::class.java)
    // private val TOKEN = UserSession.getToken()

    override suspend fun addCoach(coach: Coach): Result<String> {
        val addCoach = c.addCoach(coach.toCoachDto())
        return if (addCoach.isSuccessful) {
            Result.success("Coach added successfully")
        } else {
            val code = addCoach.code()
            val error = addCoach.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }


    override suspend fun getAllCoaches(): Result<List<Coach>> {
        val result = c.getAllCoaches()
        return if (result.isSuccessful) {
            result.body()?.let {
                Result.success(it.map { coach -> coach.toCoach() })
            } ?: Result.success(emptyList())
        } else {
            val code = result.code()
            val error = result.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }


    override suspend fun getCoach(id: Int): Result<Coach> {
        val result = c.getCoach(id)
        return if (result.isSuccessful) {
            result.body()?.let { Result.success(it.toCoach()) }
                ?: Result.failure(Exception("NO coach found"))
        } else {
            val code = result.code()
            val error = result.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }

    override suspend fun editCoach(coach: Coach): Result<String> {
        val result = c.updateCoach(
            id = coach.id,
            firstName = coach.firstName.toTextPlainRequestBody(),
            lastName = coach.lastName.toTextPlainRequestBody(),
            phoneNumber = coach.phoneNumber.toTextPlainRequestBody(),
            bio = coach.bio.toTextPlainRequestBody(),
            salary = coach.salary.toTextPlainRequestBody(),
            specialty = coach.specialty.toTextPlainRequestBody(),
            birthDate = coach.dateOfBirth.toTextPlainRequestBody(),
            image = coach.image?.toTextPlainRequestBody()
        )
        return if (result.isSuccessful)
            Result.success("edit successfully")
        else {
            val code = result.code()
            val error = result.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }

    override suspend fun toggleCoachState(id: Int): Result<String> {
        val result = c.toggleState(
            id = id,
            //         token = TOKEN
        )
        return if (result.isSuccessful)
            Result.success("edit successfully")
        else {
            val code = result.code()
            val error = result.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }

    override suspend fun getCoachReviews(id: Int): Result<List<CoachReviews>> {
        val result = c.getCoachReview(
            //       token = TOKEN,
            id = id
        )
        return if (result.isSuccessful) {
            result.body()?.let {
                Result.success(it.map { review ->
                    review.toCoachReview()
                })
            } ?: Result.success(emptyList())
        } else {
            val code = result.code()
            val error = result.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }

    }

    override suspend fun assignCoachToService(cId: Int, sId: Int): Result<String> {
        val body = mapOf("coachId" to cId, "serviceId" to sId)
        val result = c.assignCoachToService(body = body)

        return if (result.isSuccessful)
            Result.success("edit successfully")
        else {
            val code = result.code()
            val error = result.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }

    }

    override suspend fun removeCoachFromService(cId: Int, sId: Int): Result<String> {
        val body = mapOf("coachId" to cId, "serviceId" to sId)
        val result = c.removeCoachFromService(body = body)

        return if (result.isSuccessful)
            Result.success("edit successfully")
        else {
            val code = result.code()
            val error = result.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }


    }

    private fun String.toTextPlainRequestBody(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun Int.toTextPlainRequestBody(): RequestBody {
        return this.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun CoachDTO.toCoach(): Coach {
        return Coach(
            id = id,
            firstName = firstName,
            lastName = lastName,
            salary = salary,
            avgRating = averageRating,
            specialty = specialty,
            image = image,
            bio = bio,
            dateOfBirth = dateOfBirth,
            phoneNumber = phoneNumber
        )
    }

    private fun CoachReviewDTO.toCoachReview(): CoachReviews {
        return CoachReviews(
            review = review,
            rating = rating
        )
    }

    fun Coach.toCoachDto(): CoachDTO {
        return CoachDTO(
            id = id,
            bio = bio,
            dateOfBirth = dateOfBirth,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            salary = salary,
            specialty = specialty,
            averageRating = avgRating,
            image = image,
            isDisabled = false
        )
    }

}