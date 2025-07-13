package com.fola.domain.model

data class Membership(
    val description: String,
    val durationInDays: Int,
    val membershipId: Int,
    val name: String,
    val price: Int
)