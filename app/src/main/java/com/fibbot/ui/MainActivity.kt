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
import com.fibbot.ui.screens.TradingScreen
import com.fibbot.ui.screens.ChartScreen

enum class NavigationItem {
    TRADING, CHART
}

@Composable
fun MainApp(
    tradingViewModel: TradingViewModel,
    chartViewModel: ChartViewModel
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
            }
        }
    ) { innerPadding ->
        when (selectedItem) {
            NavigationItem.TRADING -> TradingScreen(
                viewModel = tradingViewModel,
                modifier = Modifier.padding(innerPadding)
            )
            NavigationItem.CHART -> ChartScreen(
                viewModel = chartViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun TradingScreen(
    viewModel: TradingViewModel,
    modifier: Modifier = Modifier
) {
    com.fibbot.ui.screens.TradingScreen(
        viewModel = viewModel
    )
}

@Composable
fun ChartScreen(
    viewModel: ChartViewModel,
    modifier: Modifier = Modifier
) {
    com.fibbot.ui.screens.ChartScreen(
        viewModel = viewModel
    )
}