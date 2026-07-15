package com.fibbot.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fibbot.viewmodel.TradingViewModel
import com.fibbot.models.TradeEntity

@Composable
fun TradingScreen(viewModel: TradingViewModel) {
    val isTrading by viewModel.isTrading.collectAsState()
    val currentTrades by viewModel.currentTrades.collectAsState()
    val totalProfitLoss by viewModel.totalProfitLoss.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "Trading Bot",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Portfolio Stats
        PortfolioStats(
            totalPL = totalProfitLoss,
            openTrades = currentTrades.size
        )

        // Control Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.analyzeAndTrade("BTCUSDT") },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                enabled = !isTrading
            ) {
                Text(if (isTrading) "Analyzing..." else "Start Trading")
            }

            Button(
                onClick = { viewModel.closeOpenTrades() },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                enabled = currentTrades.isNotEmpty()
            ) {
                Text("Close All")
            }
        }

        // Active Trades List
        if (currentTrades.isNotEmpty()) {
            Text(
                text = "Active Trades (${currentTrades.size})",
                style = MaterialTheme.typography.titleMedium
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(currentTrades.size) { index ->
                    TradeCard(trade = currentTrades[index])
                }
            }
        } else {
            Text(
                text = "No Active Trades",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(32.dp)
            )
        }
    }
}

@Composable
fun PortfolioStats(
    totalPL: Double,
    openTrades: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Total P&L", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "$${'$'}%.2f".format(totalPL),
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (totalPL >= 0) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
                )
            }
            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Open Trades", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = openTrades.toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}

@Composable
fun TradeCard(trade: TradeEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${trade.symbol} - ${trade.side}",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Entry: $${'$'}%.2f".format(trade.entryPrice),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SL: $${'$'}%.2f | TP: $${'$'}%.2f".format(trade.stopLoss, trade.takeProfit),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Qty: %.4f".format(trade.quantity),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun LazyColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable LazyListScope.() -> Unit
) {
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        content = content
    )
}