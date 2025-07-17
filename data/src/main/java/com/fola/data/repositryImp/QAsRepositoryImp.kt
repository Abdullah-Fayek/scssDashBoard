package com.fola.data.repositryImp

import com.fola.data.remote.api.QAsApi
import com.fola.data.remote.dto.QAsDTO
import com.fola.data.remote.retrofitBuilder.retrofit
import com.fola.domain.model.QA
import com.fola.domain.repo.QAsRepository


class QAsRepositoryImp : QAsRepository{
    private val c = retrofit.create(QAsApi::class.java)

    override suspend fun getAllQAs(): Result<List<QA>> {
        val r = c.getAllQAs()
        return if (r.isSuccessful) {
            r.body()?.let {
                Result.success(it.map { qAsDTO ->
                    qAsDTO.toQA()
                })
            } ?: Result.success(emptyList())
        } else {
            val code = r.code()
            val error = r.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }

    override suspend fun getQAById(qa: QA): Result<QA> {
        val id = qa.id ?: return Result.failure(Exception("id is null"))
        val r = c.getQAs(id)
        return if (r.isSuccessful) {
            val body = r.body()
            if (body != null) Result.success(body.toQA())
            else Result.failure(Exception("No QA"))
        } else {
            val code = r.code()
            val error = r.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }

    override suspend fun editeQA(qa: QA): Result<Unit> {
        val id = qa.id ?: return Result.failure(Exception("id is null"))
        val r = c.editQAs(
            id = id, qAs = qa
        )
        return if (r.isSuccessful) {
            Result.success(Unit)
        } else {
            val code = r.code()
            val error = r.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))
        }
    }

    override suspend fun deleteQA(qa: QA): Result<Unit> {
        val id = qa.id ?: return Result.failure(Exception("id is null"))
        val r = c.deleteQAs(id)
        return if (r.isSuccessful) Result.success(Unit)
        else {
            val code = r.code()
            val error = r.errorBody()?.string()
            Result.failure(Exception("coach failed: $code ${error ?: "No error body"}"))

        }
    }

    private fun QAsDTO.toQA(): QA {
        return QA(
            id = id, question = question, answer = answer
        )
    }
}
