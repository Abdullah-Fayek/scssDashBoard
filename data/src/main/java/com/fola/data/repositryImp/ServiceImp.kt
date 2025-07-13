package com.fola.data.repositryImp

import com.fola.data.remote.api.ServicesApi
import com.fola.data.remote.dto.ServiceDTO
import com.fola.data.remote.dto.ServiceReviewDTO
import com.fola.data.remote.retrofitBuilder.retrofit
import com.fola.domain.model.Service
import com.fola.domain.model.ServiceReview
import com.fola.domain.repo.ServicesRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ServiceImp : ServicesRepository {
    private val c = retrofit.create(ServicesApi::class.java)


    override suspend fun addService(s: Service): Result<String> {
        val r = c.addService(
            name = s.name.toTextPlainRequestBody(),
            description = s.description.toTextPlainRequestBody(),
            price = s.price.toTextPlainRequestBody()
        )
        return if (r.isSuccessful)
            Result.success("Added successfully")
        else {
            val code = r.code()
            val error = r.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }


    override suspend fun editService(s: Service): Result<String> {
        val id = s.id ?: return Result.failure(Exception("id is null"))
        val r = c.editService(
            id = id,
            name = s.name.toTextPlainRequestBody(),
            description = s.description.toTextPlainRequestBody(),
            price = s.price.toTextPlainRequestBody()
        )
        return if (r.isSuccessful)
            Result.success("Edited successfully")
        else {
            val code = r.code()
            val error = r.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }

    override suspend fun getAllServices(): Result<List<Service>> {
        val r = c.getAllServices()
        return if (r.isSuccessful) {
            r.body()?.let {
                Result.success(it.map { service ->
                    service.toService()
                })
            } ?: Result.success(emptyList())
        } else {
            val code = r.code()
            val error = r.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))

        }
    }

    override suspend fun removeService(s: Service): Result<String> {
        val id = s.id ?: return Result.failure(Exception("id is null"))
        val r = c.deleteService(id)
        return if (r.isSuccessful)
            Result.success("Deleted successfully")
        else {
            val code = r.code()
            val error = r.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }


    override suspend fun getServices(s: Service): Result<Service> {
        val id = s.id ?: return Result.failure(Exception("id is null"))
        val r = c.getService(id)

        return if (r.isSuccessful) {
            val body = r.body()
            if (body != null)
                Result.success(body.toService())
            else
                Result.failure(Exception("No service"))
        } else {
            val code = r.code()
            val error = r.errorBody()?.string()
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

    override suspend fun getServiceReviews(s: Service): Result<List<ServiceReview>> {
        val id = s.id ?: return Result.failure(Exception("id is null"))
        val r = c.getServiceReviews(id)

        return if (r.isSuccessful) {
            r.body()?.let {
                Result.success(it.map { review ->
                    review.toServiceReview()
                })
            } ?: Result.success(emptyList())
        } else {
            val code = r.code()
            val error = r.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }


    private fun String.toTextPlainRequestBody(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun Int.toTextPlainRequestBody(): RequestBody {
        return this.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    }


    private fun ServiceDTO.toService(): Service {
        return Service(
            id = id,
            name = name,
            price = price,
            description = description,
            averageRating = averageRating
        )
    }

    private fun ServiceReviewDTO.toServiceReview(): ServiceReview {
        return ServiceReview(
            rating = rating,
            review = review,
            serviceId = serviceId
        )

    }
}
