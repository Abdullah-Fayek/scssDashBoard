package com.fola.domain.model

data class ServiceReview(
    val rating: Int,
    val review: String,
    val serviceId: Int
)