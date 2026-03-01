package com.projach.videogametracker.data.source.remote

import android.content.Context
import com.projach.videogametracker.BuildConfig
import com.projach.videogametracker.data.source.local.DataStore
import com.projach.videogametracker.domain.GRANT_TYPE
import com.projach.videogametracker.domain.getOrNull
import com.projach.videogametracker.domain.onSuccess
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.Instant
import javax.inject.Inject

class IgdbTokenManager @Inject constructor(
    @ApplicationContext context: Context,
    private val twitchApi: TwitchApi
) {
    private val mutex = Mutex()
    private val dataStore by lazy { DataStore(context) }

    suspend fun getValidToken(): String? = mutex.withLock {
        val expirationTimeMillis = dataStore.getLongFromStorage(TOKEN_EXPIRATION_KEY)
        val expirationTime = Instant.ofEpochMilli(expirationTimeMillis)
        val currentTime = Instant.now()

        if (!currentTime.isAfter(expirationTime)){
            dataStore.getStringFromStorage(TOKEN_KEY)?.let {
                return it
            }
        }

        //if there is no token from storage do api call
        return apiCallAsDataResult {
            twitchApi.getAuthToken(
                clientSecret = BuildConfig.TWITCH_CLIENT_SECRET,
                clientId = BuildConfig.IGDB_CLIENT_ID,
                grantType = GRANT_TYPE
            )
        }.onSuccess {
            it?.expiresIn?.let { expiresIn ->
                saveExpirationDate(expiresIn)
            }

            //save token from server
            it?.accessToken?.let { token ->
                dataStore.saveStringToStorage(TOKEN_KEY, token, true)
            }
        }.getOrNull()?.accessToken
    }

    private fun saveExpirationDate(expiresIn: Long){
        val expirationTime = Instant.now().plusSeconds(expiresIn)

        dataStore.saveLongToStorage(
            TOKEN_EXPIRATION_KEY,
            expirationTime.toEpochMilli()
        )
    }

    companion object {
        private const val TOKEN_KEY = "TOKEN_KEY"
        private const val TOKEN_EXPIRATION_KEY = "TOKEN_EXPIRATION_KEY"
    }
}