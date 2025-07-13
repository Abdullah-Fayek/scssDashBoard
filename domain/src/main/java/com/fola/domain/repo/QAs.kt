package com.fola.domain.repo

import com.fola.domain.model.QA

interface QAs {
    suspend fun addNewQAs(q: QA): Result<Unit>
    suspend fun getQAss(): Result<List<QA>>
    suspend fun getQAs(id: Int): Result<QA>
    suspend fun editQAs(id: Int): Result<Unit>
    suspend fun deleteQAs(id: Int): Result<Unit>
}