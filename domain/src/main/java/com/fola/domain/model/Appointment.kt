package com.fola.domain.model


data class Appointment(
    val coachId: Int,
    val day: String,
    val id: Int?,
    val maxAttenderNum: Int,
    val serviceId: Int?,
    val time: String,
    val duration : Int,
    val currentAttendance : Int?,
)
data class _Appointment(
    val id: Int,
    val coachName : String,
    val serviceName: String,
    val day: String,
    val time: String,
    val duration: Int,
    val maxAttenderNum: Int,
    val currentAttendance: Int = 0,
)


