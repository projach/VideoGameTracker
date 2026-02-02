package com.projach.videogametracker.data.source.local

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.projach.videogametracker.data.source.local.security.Encryption

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

    fun getFromStorage(key: String, shouldDecrypt: Boolean): String? = runCatching {
        val item = dataStore.getString(key, null) ?: return null
        when(shouldDecrypt){
            true -> Encryption.decrypt(item)
            false -> item
        }
    }.getOrElse { e ->
        Log.d("TokenDataStore", "Could not get data with exception $e")
        null
    }

    companion object {
        private const val SHARED_PREFERENCES_KEY = "video_games_app_preference"
    }
}