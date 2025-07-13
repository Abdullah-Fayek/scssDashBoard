package com.fola.data.session

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fola.data.remote.api.UserApi
import com.fola.data.remote.retrofitBuilder.retrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.IOException


@SuppressLint("StaticFieldLeak")
data object UserSession {

    private lateinit var context: Context
    private val Context.dataStore by preferencesDataStore(name = "session_prefs")
    private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_key")
    private val userRetrofit = retrofit.create(UserApi::class.java)

    private var _sessionToken: String = ""

    fun init(appContext: Context) {
        if (!::context.isInitialized)
            context = appContext.applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            _sessionToken = getSessionToken()
        }
    }


    suspend fun saveLogin(sessionToken: String, refreshToken: String) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN_KEY] = true
            prefs[TOKEN_KEY] = sessionToken
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    fun isLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[IS_LOGGED_IN_KEY] == true &&
                    prefs[TOKEN_KEY]?.isNotEmpty() == true &&
                    prefs[REFRESH_TOKEN_KEY]?.isNotEmpty() == true
        }
    }


    suspend fun clearSession() {
        val currentToken = getSessionToken()
        val currentRefreshToken = getRefreshToken()

        try {
            userRetrofit.logOut(
                mapOf(
                    "token" to currentToken,
                    "refreshToken" to currentRefreshToken
                )
            )
            Log.d("UserSession", "Logout API call successful.")
        } catch (e: IOException) {
            Log.e("UserSession", "Network error during logout: ${e.message}")
        } catch (e: Exception) {
            Log.e("UserSession", "Error during logout API call: ${e.message}", e)
        } finally {
            context.dataStore.edit {
                it[IS_LOGGED_IN_KEY] = false
                it[TOKEN_KEY] = ""
                it[REFRESH_TOKEN_KEY] = ""
            }
            Log.d("UserSession", "Local session data cleared.")
        }
    }

    suspend fun getRefreshToken(): String {
        return context.dataStore.data.map {
            it[REFRESH_TOKEN_KEY] ?: ""
        }.first()
    }

    suspend fun getSessionToken(): String {
        return context.dataStore.data.map {
            it[TOKEN_KEY] ?: ""
        }.first()
    }

    suspend fun setSessionToken(token: String) {
            _sessionToken = token
            context.dataStore.edit { prefs ->
                prefs[TOKEN_KEY] = token
        }
    }

    suspend fun setRefreshToken(refreshToken: String) {
            context.dataStore.edit { prefs ->
                prefs[REFRESH_TOKEN_KEY] = refreshToken

        }
    }

    suspend fun refreshKey(): Result<Boolean> {
        val token = getSessionToken()
        val refreshToken = getRefreshToken()

        Log.d("session-token", "Starting refreshKey with token=$token, refresh=$refreshToken")

        if (token.isEmpty() || refreshToken.isEmpty()) {
            Log.d("session-token", "Token or refresh token is empty. Clearing session.")
            clearSession()
            return Result.failure(Exception("Token or Refresh Token is empty. Clearing session."))
        }

        try {
            Log.d("session-token", "Sending refresh request to API")
            val response = userRetrofit.refreshToken(
                mapOf("token" to token, "refreshToken" to refreshToken),
                token = "Bearer $token"
            )
            Log.d(
                "session-token",
                "Received response: code=${response.code()}, message=${response.message()}"
            )

            if (response.isSuccessful) {
                val body = response.body()
                Log.d("session-token", "Response body: $body")
                if (body != null) {
                        setSessionToken(body.token)
                        setRefreshToken(body.refreshToken)
                    return Result.success(true)
                } else {
                    Log.e("session-token", "API success but no body")
                    clearSession()
                    return Result.failure(Exception("API success but no body"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "No error body"
                Log.e("session-token", "Refresh failed with code=${response.code()}: $errorBody")
                clearSession()
                return Result.failure(Exception("Refresh failed: $errorBody"))
            }
        } catch (e: IOException) {
            Log.e("session-token", "Network error during refresh: ${e.message}", e)
            return Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Log.e("session-token", "Unexpected error during refresh: ${e.message}", e)
            return Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }

    fun getToken(): String {
        return _sessionToken
    }


}