package com.fola.data.remote.dto

data class CoachDTO(
    val averageRating: Int,
    val firstName: String,
    val id: Int,
    val image: String?,
    val lastName: String,
    val specialty: String,
    val bio : String = ""
)
