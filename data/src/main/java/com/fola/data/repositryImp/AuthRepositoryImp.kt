package com.fola.data.repositryImp

import com.fola.data.remote.api.UserApi
import com.fola.data.remote.dto.UserDTO
import com.fola.data.remote.retrofitBuilder.retrofit
import com.fola.data.session.UserSession
import com.fola.domain.model.User
import com.fola.domain.repo.AuthRepository
import java.io.IOException

class AuthRepositoryImp : AuthRepository {


    override suspend fun login(userName: String, password: String): Result<User> {

        try {
            val c = retrofit.create(UserApi::class.java)
            val session = UserSession
            val response = c.logIn(
                mapOf(
                    "phoneNumber" to userName,
                    "password" to password
                )
            )

            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    session.saveLogin(
                        sessionToken = user.token,
                        refreshToken = user.refreshToken
                    )
                    return Result.success(user.user())
                } else {
                    val code = response.code()
                    val error = response.errorBody()?.string()
                    return Result.failure(
                        Exception("Login failed. Server returned an empty response. [Code $code] ${error ?: ""}")
                    )
                }
            } else {
                val code = response.code()
                val error = response.errorBody()?.string()

                val message = when (code) {
                    400 -> "Invalid phone number or password. Please double-check and try again."
                    401 -> "Unauthorized. Are you sure your credentials are correct?"
                    403 -> "Access denied. You don't have permission to log in."
                    404 -> "Server not found. Please try again later."
                    500 -> "Server error. Try again after a while."
                    else -> "Login failed with error code $code. ${error ?: "No details provided."}"
                }

                return Result.failure(Exception(message))
            }

        } catch (e: IOException) {
            // No internet, timeout, etc.
            return Result.failure(Exception("Couldn’t connect. Please check your internet connection."))
        } catch (e: Exception) {
            return Result.failure(Exception("Something went wrong: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }

    override suspend fun editUser(user: User): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun changePassword(oPassword: String, nPassword: String): Result<String> {
        TODO("Not yet implemented")
    }

    private fun UserDTO.user(): User {
        return User(
            id = id,
            firstName = firstName,
            lastName = lastName,
            dateOfBirth = dateOfBirth,
            phoneNumber = phoneNumber,
        )

    }
}