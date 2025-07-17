package com.fola.data.repositryImp

import android.util.Base64
import android.util.Log
import com.fola.data.remote.api.CoachApi
import com.fola.data.remote.dto.CoachDTO
import com.fola.data.remote.dto.CoachReviewDTO
import com.fola.data.remote.retrofitBuilder.retrofit
import com.fola.data.session.UserSession
import com.fola.domain.model.Coach
import com.fola.domain.model.CoachReviews
import com.fola.domain.repo.CoachRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CoachRepositoryImp : CoachRepository {

    private val c = retrofit.create(CoachApi::class.java)
    // private val TOKEN = UserSession.getToken()

    override suspend fun addCoach(coach: Coach): Result<String> {

        Log.d("refreshKey", "addCoach: ${UserSession.getRefreshToken()}")
        Log.d("refreshKey", "addCoach: ${UserSession.getToken()}")
        val result = c.addCoach(
            firstName = coach.firstName.toTextPlainRequestBody(),
            lastName = coach.lastName.toTextPlainRequestBody(),
            specialty = coach.specialty.toTextPlainRequestBody(),
            bio = coach.bio.toTextPlainRequestBody(),
            image = coach.image?.toMultipartPartOrNull()
        )

        Log.d("CoachRepositoryImp", "addCoach: ${result.body()}")

        return if (result.isSuccessful) {
            Result.success("Coach added successfully")
        } else {
            val code = result.code()
            val error = result.errorBody()?.string()
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
        Log.d("refreshKey", "editCoach: ${UserSession.getRefreshToken()}")
        Log.d("refreshKey", "editCoach: ${UserSession.getToken()}")
        Log.d("CoachRepositoryImp", "editCoach: ${coach.toString()}")
        val result = c.updateCoach(
            id = coach.id,
            firstName = coach.firstName.toPlainTextBody(),
            lastName = coach.lastName.toPlainTextBody(),
            specialty = coach.specialty.toPlainTextBody(),
            bio = coach.bio.toPlainTextBody(),
            image = emptyImageBody()
        )

        return if (result.isSuccessful) {
            Result.success("edit successfully")
        } else {
            val error = try {
                result.errorBody()?.string()
            } catch (e: Exception) {
                "Failed to parse error: ${e.message}"
            }

            Log.e("CoachRepositoryImp", "editCoach failed: ${result.code()} $error")
            Result.failure(Exception("Coach update failed: ${result.code()} $error"))
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

    private fun emptyImageRequestBody(): RequestBody {
        return "".toRequestBody("text/plain".toMediaTypeOrNull())
    }


    fun String.toPlainTextBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaType())

    fun Int.toPlainTextBody(): RequestBody =
        this.toString().toPlainTextBody()

    fun emptyImageBody(): RequestBody =
        "".toRequestBody("text/plain".toMediaType())


    private fun String.toMultipartPartOrNull(): MultipartBody.Part? {
        return try {
            val imageBytes = Base64.decode(this, Base64.DEFAULT)
            val tempFile = File.createTempFile("coach_img", ".jpg")
            tempFile.writeBytes(imageBytes)
            val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("Image", tempFile.name, requestFile)
        } catch (e: Exception) {
            null
        }
    }


    private fun CoachDTO.toCoach(): Coach {
        return Coach(
            id = id,
            firstName = firstName,
            lastName = lastName,
            avgRating = averageRating,
            specialty = specialty,
            image = image,
            bio = bio
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
            firstName = firstName,
            lastName = lastName,
            specialty = specialty,
            averageRating = avgRating,
            image = image,
        )
    }

}