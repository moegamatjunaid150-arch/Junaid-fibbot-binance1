package com.fibbot.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import timber.log.Timber

/**
 * Manages Binance API keys securely using EncryptedSharedPreferences.
 * Keys are never hardcoded — users must enter them via the Settings screen.
 * Data is encrypted at rest using AES256-GCM (key) and AES256-SIV (value).
 */
class ApiKeyManager(private val context: Context) {
    private var encryptedStorageAvailable = true

    private val prefs: SharedPreferences by lazy {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            encryptedStorageAvailable = false
            Timber.w(e, "EncryptedSharedPreferences unavailable; falling back to plain storage")
            context.getSharedPreferences(PREFS_NAME + "_plain", Context.MODE_PRIVATE)
        }
    }

    fun getApiKey(): String = prefs.getString(KEY_API_KEY, "") ?: ""

    fun getApiSecret(): String = prefs.getString(KEY_API_SECRET, "") ?: ""

    fun saveApiKey(apiKey: String) {
        prefs.edit().putString(KEY_API_KEY, apiKey.trim()).apply()
    }

    fun saveApiSecret(apiSecret: String) {
        prefs.edit().putString(KEY_API_SECRET, apiSecret.trim()).apply()
    }

    fun clearKeys() {
        prefs.edit()
            .remove(KEY_API_KEY)
            .remove(KEY_API_SECRET)
            .apply()
    }

    fun hasApiKeys(): Boolean = getApiKey().isNotEmpty() && getApiSecret().isNotEmpty()

    fun isEncryptedStorageAvailable(): Boolean {
        prefs
        return encryptedStorageAvailable
    }

    companion object {
        private const val PREFS_NAME = "fibbot_api_keys_encrypted"
        private const val KEY_API_KEY = "binance_api_key"
        private const val KEY_API_SECRET = "binance_api_secret"
    }
}
