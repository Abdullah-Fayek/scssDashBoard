package com.fola.data.session

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fola.data.remote.api.UserApi
import com.fola.data.remote.dto.ProfileDTO
import com.fola.data.remote.retrofitBuilder.retrofit
import com.fola.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private var user: ProfileDTO? = null


    private var _sessionToken: String = ""

    fun init(appContext: Context) {
        if (!::context.isInitialized)
            context = appContext.applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            _sessionToken = getSessionToken()
            setUser()
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
            Log.d("UserSession", "Network error during logout: ${e.message}")
        } catch (e: Exception) {
            Log.d("UserSession", "Error during logout API call: ${e.message}", e)
        } finally {
            context.dataStore.edit {
                it[IS_LOGGED_IN_KEY] = false
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

        _isRefreshing.value = true
        val token = getSessionToken()
        val refreshToken = getRefreshToken()


        Log.d("session-token", "Starting refreshKey with token=$token, refresh=$refreshToken")

        if (token.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
            Log.e(
                "session-token",
                "Can't refresh: token or refreshToken is blank. Session will be cleared."
            )
            _isRefreshing.value = false
            clearSession()
            return Result.failure(IllegalStateException("Can't refresh: Missing token or refreshToken"))
        }

        return try {
            val response = userRetrofit.refreshToken(
                mapOf("token" to token, "refreshToken" to refreshToken),
            )
            Log.d(
                "session-token",
                "Received response: code=${response.code()}, message=${response.message()}"
            )

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    setSessionToken(body.token)
                    setRefreshToken(body.refreshToken)
                    _isRefreshing.value = false
                    return Result.success(true)

                } else {
                    Log.w("session-token", "API success but no body")
                    clearSession()
                    _isRefreshing.value = false
                    return Result.failure(Exception("API success but no body"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "No error body"
                Log.w("session-token", "Refresh failed with code=${response.code()}: $errorBody")
                clearSession()
                _isRefreshing.value = false
                return Result.failure(Exception("Refresh failed: $errorBody"))
            }
        } catch (e: IOException) {
            Log.w("session-token", "Network error during refresh: ${e.message}", e)
            _isRefreshing.value = false
            return Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Log.w("session-token", "Unexpected error during refresh: ${e.message}", e)
            _isRefreshing.value = false
            return Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }


    fun getToken(): String {
        return _sessionToken
    }

    private suspend fun setUser() {
        val r = userRetrofit.getUser()
        if (r.isSuccessful) {
            user = r.body()
            Log.d("UserSession", "User data fetched successfully: $user")
        } else {
            Log.e("UserSession", "Failed to fetch user data: ${r.errorBody()?.string()}")
        }
    }

    fun getUser(): ProfileDTO {
        if (user == null) {
            CoroutineScope(Dispatchers.IO).launch {
                setUser()
            }
        }
        return user ?: ProfileDTO(
            membershipNumber = "Unknown",
            userName = "unknown",
            firstName = "Guest",
            lastName = "Guest"
        )
    }


}