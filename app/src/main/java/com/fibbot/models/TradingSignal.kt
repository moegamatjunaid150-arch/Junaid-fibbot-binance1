package com.fibbot.models

data class TradingSignal(
    val symbol: String,
    val signalType: SignalType,
    val strength: Float,
    val price: Double,
    val timestamp: Long,
    val confidence: Float,
    val indicators: IndicatorValues,
    val fibonacci: FibonacciLevels
)

enum class SignalType {
    BUY, SELL, HOLD
}

data class IndicatorValues(
    val ema9: Double,
    val ema21: Double,
    val rsi14: Double,
    val macdLine: Double,
    val macdSignal: Double,
    val macdHistogram: Double
)

data class FibonacciLevels(
    val swingHigh: Double,
    val swingLow: Double,
    val level0: Double,
    val level236: Double,
    val level382: Double,
    val level5: Double,
    val level618: Double,
    val level786: Double,
    val level100: Double
)

data class ChartData(
    val symbol: String,
    val interval: String,
    val candles: List<CandleData>,
    val indicators: List<IndicatorData>,
    val signals: List<TradingSignal>
)

data class CandleData(
    val timestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double
)

data class IndicatorData(
    val timestamp: Long,
    val value: Double,
    val type: String
)

data class TradeStats(
    val totalTrades: Int = 0,
    val winningTrades: Int = 0,
    val losingTrades: Int = 0,
    val totalProfit: Double = 0.0,
    val totalLoss: Double = 0.0,
    val winRate: Float = 0f,
    val averageWin: Double = 0.0,
    val averageLoss: Double = 0.0,
    val profitFactor: Double = 0.0,
    val maxDrawdown: Double = 0.0,
    val largestWin: Double = 0.0,
    val largestLoss: Double = 0.0
)