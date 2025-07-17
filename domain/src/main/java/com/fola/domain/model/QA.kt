package com.fola.domain.model

data class QA(
    val id : Int?,
    val question: String,
    val answer: String?,
    val SortNum : Int = 1
)
