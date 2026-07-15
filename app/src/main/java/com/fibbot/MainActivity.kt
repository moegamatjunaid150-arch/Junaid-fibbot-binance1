package com.fibbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fibbot.di.DependencyInjection
import com.fibbot.ui.MainApp
import com.fibbot.ui.theme.FibBotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tradingViewModel = DependencyInjection.provideTradingViewModel()
        val chartViewModel = DependencyInjection.provideChartViewModel()

        setContent {
            FibBotTheme {
                MainApp(
                    tradingViewModel = tradingViewModel,
                    chartViewModel = chartViewModel
                )
            }
        }
    }
}
