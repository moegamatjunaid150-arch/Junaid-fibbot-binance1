package com.fibbot.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.fibbot.viewmodel.TradingViewModel
import com.fibbot.viewmodel.ChartViewModel
import com.fibbot.viewmodel.SettingsViewModel

enum class NavigationItem {
    TRADING, CHART, SETTINGS
}

@Composable
fun MainApp(
    tradingViewModel: TradingViewModel,
    chartViewModel: ChartViewModel,
    settingsViewModel: SettingsViewModel
) {
    var selectedItem by remember { mutableStateOf(NavigationItem.TRADING) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedItem == NavigationItem.TRADING,
                    onClick = { selectedItem = NavigationItem.TRADING },
                    label = { Text("Trading") },
                    icon = { Text("T") }
                )
                NavigationBarItem(
                    selected = selectedItem == NavigationItem.CHART,
                    onClick = { selectedItem = NavigationItem.CHART },
                    label = { Text("Chart") },
                    icon = { Text("C") }
                )
                NavigationBarItem(
                    selected = selectedItem == NavigationItem.SETTINGS,
                    onClick = { selectedItem = NavigationItem.SETTINGS },
                    label = { Text("Settings") },
                    icon = { Text("⚙") }
                )
            }
        }
    ) { innerPadding ->
        when (selectedItem) {
            NavigationItem.TRADING -> com.fibbot.ui.screens.TradingScreen(
                viewModel = tradingViewModel,
                modifier = Modifier.padding(innerPadding)
            )
            NavigationItem.CHART -> com.fibbot.ui.screens.ChartScreen(
                viewModel = chartViewModel,
                modifier = Modifier.padding(innerPadding)
            )
            NavigationItem.SETTINGS -> com.fibbot.ui.screens.SettingsScreen(
                viewModel = settingsViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}