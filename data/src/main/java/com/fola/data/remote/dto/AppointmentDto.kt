package com.fola.data.remote.dto

data class AppointmentDto(
    val coachId: Int,
    val currentAttenderNum: Int?,
    val serviceId: Int?,
    val day: String,
    val duration: Int,
    val id: Int?,
    val maxAttenderNum: Int,
    val time: String
)