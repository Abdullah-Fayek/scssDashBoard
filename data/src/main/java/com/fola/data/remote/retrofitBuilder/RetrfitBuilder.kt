package com.fola.data.remote.retrofitBuilder

import android.util.Log
import com.fola.data.session.UserSession
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val token = UserSession.getToken()
        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}

class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d("TokenAuthenticator", "Authenticating for response: ${response.code}")

        if (responseCount(response) >= 2) {
            Log.w("TokenAuthenticator", "Too many retries. Bailing.")
            return null
        }

        if (response.request.url.encodedPath.contains("refresh", ignoreCase = true)) {
            Log.w("TokenAuthenticator", "Refusing to refresh during refresh call. Bailing.")
            return null
        }

        return try {
            Log.d("TokenAuthenticator", "Trying to refresh token...")
            val refreshResult = runBlocking { UserSession.refreshKey() }

            if (refreshResult.isSuccess) {
                val newToken = UserSession.getToken()
                Log.d("TokenAuthenticator", "Token refreshed! Retrying original request.")
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
            } else {
                Log.e("TokenAuthenticator", "Refresh failed. Forcing logout.")
                runBlocking { UserSession.clearSession() }
                null
            }
        } catch (e: Exception) {
            Log.e("TokenAuthenticator", "Error during token refresh: ${e.message}")
            runBlocking { UserSession.clearSession() }
            return null
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}

val client = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor())
    .authenticator(TokenAuthenticator())
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .writeTimeout(10, TimeUnit.SECONDS)
    .writeTimeout(10, TimeUnit.SECONDS)
    .build()


val retrofit: Retrofit = Retrofit
    .Builder()
    .baseUrl("https://club-api.runasp.net")
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

