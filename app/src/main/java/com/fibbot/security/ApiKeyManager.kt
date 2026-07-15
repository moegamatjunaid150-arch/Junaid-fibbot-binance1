package com.fibbot.security

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages Binance API keys securely using SharedPreferences.
 * Keys are never hardcoded — users must enter them via the Settings screen.
 * For production, consider migrating to EncryptedSharedPreferences for at-rest encryption.
 */
class ApiKeyManager(private val context: Context) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
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

    companion object {
        private const val PREFS_NAME = "fibbot_api_keys"
        private const val KEY_API_KEY = "binance_api_key"
        private const val KEY_API_SECRET = "binance_api_secret"
    }
}
