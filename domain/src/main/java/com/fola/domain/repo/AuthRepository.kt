package com.fola.domain.repo

import com.fola.domain.model.User

interface AuthRepository {
    suspend fun login(
        userName: String,
        password: String
    ): Result<User>


    suspend fun editUser(user: User): Result<String>
    suspend fun changePassword(oPassword: String, nPassword: String): Result<String>
}