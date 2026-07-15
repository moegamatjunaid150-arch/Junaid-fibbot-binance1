package com.fibbot.strategy

import kotlin.math.abs

class RiskManager(
    private val accountBalance: Double = 1000.0,
    private val riskPercentPerTrade: Double = 0.02,
    private val maxDailyLossPercent: Double = 0.05
) {
    fun calculatePositionSize(
        entryPrice: Double,
        stopLoss: Double
    ): Double {
        val riskAmount = accountBalance * riskPercentPerTrade
        val pipsRisk = abs(entryPrice - stopLoss)
        return if (pipsRisk > 0) riskAmount / pipsRisk else 0.0
    }

    fun calculateStopLoss(
        entryPrice: Double,
        atrValue: Double = 0.0,
        percentRisk: Double = 0.02
    ): Double {
        val riskAmount = if (atrValue > 0) atrValue * 2 else entryPrice * percentRisk
        return entryPrice - riskAmount
    }

    fun calculateTakeProfit(
        entryPrice: Double,
        riskRewardRatio: Double = 2.0,
        stopLoss: Double = 0.0
    ): Double {
        val risk = abs(entryPrice - stopLoss)
        return entryPrice + (risk * riskRewardRatio)
    }

    fun calculateProfitLoss(
        entryPrice: Double,
        exitPrice: Double,
        quantity: Double,
        side: String
    ): Pair<Double, Double> {
        val profitLoss = if (side == "BUY") {
            (exitPrice - entryPrice) * quantity
        } else {
            (entryPrice - exitPrice) * quantity
        }
        val profitLossPercent = ((exitPrice - entryPrice) / entryPrice) * 100
        return Pair(profitLoss, profitLossPercent)
    }

    fun canOpenTrade(currentDailyLoss: Double): Boolean {
        val maxDailyLoss = accountBalance * maxDailyLossPercent
        return abs(currentDailyLoss) <= maxDailyLoss
    }

    fun validateTradeSize(quantity: Double): Boolean {
        val maxQuantity = (accountBalance * 0.1) / 100
        return quantity <= maxQuantity && quantity > 0
    }
}