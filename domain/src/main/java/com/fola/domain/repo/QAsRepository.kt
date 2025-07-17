package com.fola.domain.repo

import com.fola.domain.model.QA

interface QAsRepository {
    suspend fun getAllQAs(): Result<List<QA>>
    suspend fun getQAById(qa: QA): Result<QA>
    suspend fun editeQA(qa: QA): Result<Unit>
    suspend fun deleteQA(qa: QA): Result<Unit>

}