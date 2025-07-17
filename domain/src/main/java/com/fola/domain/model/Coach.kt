package com.fola.domain.model

data class Coach(
    val id: Int,
    val bio: String,
    val firstName: String,
    val lastName: String,
    val specialty: String,
    val image: String? = null,
    val avgRating: Int = 0
)


