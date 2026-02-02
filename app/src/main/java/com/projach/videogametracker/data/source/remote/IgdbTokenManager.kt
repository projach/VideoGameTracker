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
import javax.inject.Inject

class IgdbTokenManager @Inject constructor(
    @ApplicationContext context: Context,
    private val twitchApi: TwitchApi
) {
    private val mutex = Mutex()
    private val dataStore by lazy { DataStore(context) }

    suspend fun getValidToken(): String? = mutex.withLock {
        //check if there is token in storage
        dataStore.getFromStorage(SHARED_PREFERENCES_AUTH_KEY, true)?.let { return it }

        //if there is no token from storage do api call
        return apiCallAsDataResult {
            twitchApi.getAuthToken(
                clientSecret = BuildConfig.TWITCH_CLIENT_SECRET,
                clientId = BuildConfig.IGDB_CLIENT_ID,
                grantType = GRANT_TYPE
            )
        }.onSuccess {
            //TODO(save expiration date)

            //save token from server
            it?.accessToken?.let { token ->
                dataStore.saveStringToStorage(TOKEN_KEY, token, true)
            }
        }.getOrNull()?.accessToken
    }

    companion object {
        private const val TOKEN_KEY = "TOKEN_KEY"
        private const val TOKEN_EXPIRATION_KEY = "TOKEN_EXPIRATION_KEY"
        private const val SHARED_PREFERENCES_AUTH_KEY = "auth_token"
    }
}