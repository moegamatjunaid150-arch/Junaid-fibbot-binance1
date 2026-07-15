package com.fibbot.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fibbot.viewmodel.ChartViewModel
import com.fibbot.models.ChartData

@Composable
fun ChartScreen(viewModel: ChartViewModel, modifier: Modifier = Modifier) {
    val chartData by viewModel.chartData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var selectedSymbol by remember { mutableStateOf("BTCUSDT") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "Chart Analysis",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Symbol Selection
        SymbolSelector(
            selectedSymbol = selectedSymbol,
            onSymbolSelected = { symbol ->
                selectedSymbol = symbol
                viewModel.loadChartData(symbol, "1m")
            }
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else if (chartData != null) {
            ChartContent(chartData = chartData!!)
        } else {
            Text(
                text = "No data available",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun SymbolSelector(
    selectedSymbol: String,
    onSymbolSelected: (String) -> Unit
) {
    val symbols = listOf("BTCUSDT", "ETHUSDT", "BNBUSDT", "XRPUSDT")
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            shape = MaterialTheme.shapes.small
        ) {
            Text(selectedSymbol)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            symbols.forEach { symbol ->
                DropdownMenuItem(
                    text = { Text(symbol) },
                    onClick = {
                        onSymbolSelected(symbol)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ChartContent(chartData: ChartData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(androidx.compose.foundation.rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Price Section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Price Data", style = MaterialTheme.typography.titleMedium)
                if (chartData.candles.isNotEmpty()) {
                    val lastCandle = chartData.candles.last()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Open: $${'$'}${lastCandle.open}", style = MaterialTheme.typography.bodySmall)
                            Text("High: $${'$'}${lastCandle.high}", style = MaterialTheme.typography.bodySmall)
                        }
                        Column {
                            Text("Low: $${'$'}${lastCandle.low}", style = MaterialTheme.typography.bodySmall)
                            Text("Close: $${'$'}${lastCandle.close}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        // Indicators Section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Indicators", style = MaterialTheme.typography.titleMedium)
                if (chartData.ema9.isNotEmpty()) {
                    Text(
                        "EMA9: $${'$'}${String.format("%.2f", chartData.ema9.last())}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (chartData.ema21.isNotEmpty()) {
                    Text(
                        "EMA21: $${'$'}${String.format("%.2f", chartData.ema21.last())}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (chartData.rsi14.isNotEmpty()) {
                    Text(
                        "RSI14: ${String.format("%.2f", chartData.rsi14.last())}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (chartData.macdLine.isNotEmpty()) {
                    Text(
                        "MACD: ${String.format("%.4f", chartData.macdLine.last())}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}