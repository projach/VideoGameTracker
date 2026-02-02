package com.projach.videogametracker.data.source.local.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object Encryption {
    private const val KEY_ALIAS = "VideoGameTracker"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val KEY_STORE = "AndroidKeyStore"

    private fun getKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEY_STORE).apply { load(null) }

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEY_STORE)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build()
            )
            keyGenerator.generateKey()
        }
        return (keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
    }

    fun encrypt(text: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getKey())

        val iv = cipher.iv
        val encrypted = cipher.doFinal(text.toByteArray())

        return Base64.encodeToString(iv + encrypted, Base64.DEFAULT)
    }

    fun decrypt(encryptedText: String): String{
        val combined = Base64.decode(encryptedText, Base64.DEFAULT)

        val iv = combined.copyOfRange(0, 12)
        val encrypted = combined.copyOfRange(12, combined.size)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), GCMParameterSpec(128, iv))

        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted)
    }
}