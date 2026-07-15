package com.fibbot.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.fibbot.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, modifier: Modifier = Modifier) {
    val apiKey by viewModel.apiKey.collectAsState()
    val apiSecret by viewModel.apiSecret.collectAsState()
    val saveStatus by viewModel.saveStatus.collectAsState()
    val isEncryptedStorageAvailable by viewModel.isEncryptedStorageAvailable.collectAsState()
    var showApiKey by remember { mutableStateOf(false) }
    var showSecret by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "API Settings",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Binance API Keys",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Enter your Binance API key and secret. Keys are stored locally and never transmitted to third parties.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (!isEncryptedStorageAvailable) {
                    Text(
                        text = "⚠ Secure encrypted storage is unavailable on this device. Saved API keys will be stored locally without encryption.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { viewModel.updateApiKey(it) },
                    label = { Text("API Key") },
                    placeholder = { Text("Paste your Binance API key") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (showApiKey) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { showApiKey = !showApiKey }) {
                            Text(if (showApiKey) "Hide" else "Show")
                        }
                    }
                )

                OutlinedTextField(
                    value = apiSecret,
                    onValueChange = { viewModel.updateApiSecret(it) },
                    label = { Text("API Secret") },
                    placeholder = { Text("Paste your Binance API secret") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (showSecret) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { showSecret = !showSecret }) {
                            Text(if (showSecret) "Hide" else "Show")
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.saveKeys() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save Keys")
                    }
                    OutlinedButton(
                        onClick = { viewModel.clearKeys() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear Keys")
                    }
                }

                if (saveStatus.isNotEmpty()) {
                    Text(
                        text = saveStatus,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (saveStatus.startsWith("✓"))
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("How to get API keys", style = MaterialTheme.typography.titleSmall)
                Text(
                    text = "1. Log in to Binance.com\n" +
                            "2. Go to Profile → API Management\n" +
                            "3. Click 'Create API'\n" +
                            "4. Enable 'Read Info' and 'Spot & Margin Trading'\n" +
                            "5. Restrict access to your IP for security",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "⚠ Never share your API secret with anyone.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
