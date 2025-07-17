package com.fola.data.remote.dto

import com.squareup.moshi.Json

data class ProfileDTO(
    val membershipNumber: String,
    val userName: String,
    val firstName: String,
    val lastName: String,
)