package com.fola.data.repositryImp

import com.fola.domain.model.User
import com.fola.domain.repo.UserRepository

class UserRepositoryImp : UserRepository {
    override suspend fun getAllUsers(): Result<List<User>> {
        TODO("Not yet implemented")
    }


}