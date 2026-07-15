package com.fibbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fibbot.di.DependencyInjection
import com.fibbot.ui.MainApp
import com.fibbot.ui.theme.FibBotTheme
import com.fibbot.viewmodel.ChartViewModel
import com.fibbot.viewmodel.SettingsViewModel
import com.fibbot.viewmodel.TradingViewModel

class MainActivity : ComponentActivity() {
    private val tradingViewModel by viewModels<TradingViewModel> {
        dependencyFactory { DependencyInjection.provideTradingViewModel() }
    }

    private val chartViewModel by viewModels<ChartViewModel> {
        dependencyFactory { DependencyInjection.provideChartViewModel() }
    }

    private val settingsViewModel by viewModels<SettingsViewModel> {
        dependencyFactory { DependencyInjection.provideSettingsViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FibBotTheme {
                MainApp(
                    tradingViewModel = tradingViewModel,
                    chartViewModel = chartViewModel,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }

    private fun <T : ViewModel> dependencyFactory(creator: () -> T): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
        }
}
