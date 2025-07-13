package com.fola.data.remote.dto

import com.squareup.moshi.Json

data class UserDTO(

    @Json(name = "birth_Of_Date") val dateOfBirth: String,
    val firstName: String,
    val id: String,
    val lastName: String,
    val membershipNumber: String,
    val phoneNumber: String,
    val refreshToken: String,
    val token: String
)


