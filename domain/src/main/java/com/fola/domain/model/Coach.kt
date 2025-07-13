package com.fola.domain.model

data class Coach(
    val id : Int,
    val bio: String,
    val dateOfBirth: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val salary: Int,
    val specialty: String,
    val image: String?,
    val avgRating : Int = 0
)


