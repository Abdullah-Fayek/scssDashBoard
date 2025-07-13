package com.fola.data.remote.dto

data class CoachReviewDTO(
    val firstName: String,
    val lastName: String,
    val rating: Int,
    val review: String,
    val reviewAt: String
)