package com.fola.domain.repo

import com.fola.domain.model.User

interface UserRepository {

    suspend fun getAllUsers(): Result<List<User>>
}