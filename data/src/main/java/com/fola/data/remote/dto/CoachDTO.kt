package com.fola.data.remote.dto

import com.squareup.moshi.Json

data class CoachDTO(
    val averageRating: Int,
    @Json(name  = "birth_Of_Date") val dateOfBirth: String,
    val firstName: String,
    val id: Int,
    val image: String?,
    val isDisabled: Boolean,
    val lastName: String,
    val phoneNumber: String,
    val salary: Int,
    val specialty: String,
    val bio : String = ""
)
