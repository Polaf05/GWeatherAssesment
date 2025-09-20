package com.assesment.gweather.com.assesment.gweather.data.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SecureStorage(private val context: Context) {

    // DataStore instance
    private val Context.dataStore by preferencesDataStore("secure_store")

    private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")

    // AEAD (Authenticated Encryption with Associated Data)
    private val aead: Aead by lazy { initAead(context) }

    suspend fun saveToken(token: String) {
        val encrypted = encrypt(token)
        context.dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = encrypted
        }
    }

    // Flow that always returns decrypted value
    val accessToken: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_ACCESS_TOKEN]?.let { decrypt(it) }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    // --- Encryption/Decryption ---
    private fun encrypt(plainText: String): String {
        val cipher = aead.encrypt(plainText.toByteArray(), null)
        return android.util.Base64.encodeToString(cipher, android.util.Base64.NO_WRAP)
    }

    private fun decrypt(cipherText: String): String? {
        return try {
            val decoded = android.util.Base64.decode(cipherText, android.util.Base64.NO_WRAP)
            String(aead.decrypt(decoded, null))
        } catch (e: Exception) {
            null
        }
    }

    private fun initAead(context: Context): Aead {
        AeadConfig.register()
        val keysetHandle: KeysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, "tink_keyset", "tink_prefs")
            .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
            .withMasterKeyUri("android-keystore://tink_master_key")
            .build()
            .keysetHandle
        return keysetHandle.getPrimitive(Aead::class.java)
    }
}