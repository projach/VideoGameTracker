package com.projach.videogametracker.data.source.local

import android.content.Context
import androidx.core.content.edit
import com.projach.videogametracker.data.source.local.security.Encryption
import com.projach.videogametracker.utils.Logger

class DataStore(context: Context) {
    private val dataStore =
        context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)

    fun saveLongToStorage(key: String, item: Long) {
        dataStore.edit {
            putLong(key, item)
        }
    }

    fun saveStringToStorage(
        key: String,
        item: String,
        shouldEncrypt: Boolean
    ) {
        dataStore.edit {
            putString(
                key, when (shouldEncrypt) {
                    true -> Encryption.encrypt(item)
                    false -> item
                }
            )
        }
    }

    fun getStringFromStorage(key: String): String? = runCatching {
        Encryption.decrypt(dataStore.getString(key, null) ?: return null)
    }.getOrElse { e ->
        Logger.e("TokenDataStore", "Could not get data with exception $e")
        null
    }

    fun getLongFromStorage(key: String): Long = runCatching {
        dataStore.getLong(key, 0)
    }.getOrElse { e ->
        Logger.e("TokenDataStore", "Could not get data with exception $e")
        0
    }


    companion object {
        private const val SHARED_PREFERENCES_KEY = "video_games_app_preference"
    }
}