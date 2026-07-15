package com.fibbot.models

import com.fibbot.database.entity.CandleEntity

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

data class ChartData(
    val candles: List<CandleEntity>,
    val ema9: List<Double>,
    val ema21: List<Double>,
    val rsi14: List<Double>,
    val macdLine: List<Double>,
    val macdSignal: List<Double>,
    val macdHistogram: List<Double>,
    val atr: List<Double>,
    val bbSma: List<Double>,
    val bbUpper: List<Double>,
    val bbLower: List<Double>
)