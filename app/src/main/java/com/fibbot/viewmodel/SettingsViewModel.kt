package com.fibbot.viewmodel

import androidx.lifecycle.ViewModel
import com.fibbot.security.ApiKeyManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val apiKeyManager: ApiKeyManager
) : ViewModel() {

    private val _apiKey = MutableStateFlow(apiKeyManager.getApiKey())
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    private val _apiSecret = MutableStateFlow("")
    val apiSecret: StateFlow<String> = _apiSecret.asStateFlow()

    private val _saveStatus = MutableStateFlow("")
    val saveStatus: StateFlow<String> = _saveStatus.asStateFlow()

    fun updateApiKey(key: String) {
        _apiKey.value = key
        _saveStatus.value = ""
    }

    fun updateApiSecret(secret: String) {
        _apiSecret.value = secret
        _saveStatus.value = ""
    }

    fun saveKeys() {
        val key = _apiKey.value.trim()
        val secret = _apiSecret.value.trim()
        if (key.isEmpty() || secret.isEmpty()) {
            _saveStatus.value = "✗ Both API key and secret are required"
            return
        }
        apiKeyManager.saveApiKey(key)
        apiKeyManager.saveApiSecret(secret)
        _saveStatus.value = "✓ API keys saved successfully"
        _apiSecret.value = ""
    }

    fun clearKeys() {
        apiKeyManager.clearKeys()
        _apiKey.value = ""
        _apiSecret.value = ""
        _saveStatus.value = "✓ API keys cleared"
    }
}
