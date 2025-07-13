package com.fola.domain.repo

import com.fola.domain.model.Service
import com.fola.domain.model.ServiceReview

interface ServicesRepository {
    suspend fun getAllServices(): Result<List<Service>>
    suspend fun addService(s: Service): Result<String>
    suspend fun editService(s: Service): Result<String>
    suspend fun removeService(s : Service): Result<String>
    suspend fun getServices(s: Service): Result<Service>
    suspend fun assignCoachToService(cId: Int, sId: Int): Result<String>
    suspend fun removeCoachFromService(cId: Int, sId: Int): Result<String>
    suspend fun getServiceReviews(s: Service): Result<List<ServiceReview>>
}
