package com.fibbot.models

import kotlinx.serialization.Serializable

@Serializable
data class BinanceTickerPrice(
    val symbol: String,
    val price: String
)

@Serializable
data class BinanceExchangeInfo(
    val timezone: String,
    val serverTime: Long,
    val symbols: List<Map<String, Any>>
)

@Serializable
data class BinanceWebSocketKline(
    val e: String,
    val E: Long,
    val s: String,
    val k: KlineData
)

@Serializable
data class KlineData(
    val t: Long,
    val T: Long,
    val s: String,
    val i: String,
    val f: Long,
    val L: Long,
    val o: String,
    val c: String,
    val h: String,
    val l: String,
    val v: String,
    val n: Long,
    val x: Boolean,
    val q: String,
    val V: String,
    val Q: String
)

@Serializable
data class BinanceWebSocketTicker(
    val e: String,
    val E: Long,
    val s: String,
    val c: String,
    val o: String,
    val h: String,
    val l: String,
    val v: String,
    val q: String
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

data class CandleEntity(
    val id: Long = 0,
    val symbol: String,
    val interval: String,
    val timestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double
)

data class TradeEntity(
    val id: Long = 0,
    val symbol: String,
    val side: String,
    val entryPrice: Double,
    val quantity: Double,
    val stopLoss: Double,
    val takeProfit: Double,
    val entryTime: Long,
    val exitTime: Long? = null,
    val exitPrice: Double? = null,
    val status: String,
    val isPaperTrade: Boolean = true,
    val profitLoss: Double = 0.0,
    val profitLossPercent: Double = 0.0
)

data class PriceCacheEntity(
    val symbol: String,
    val price: Double,
    val timestamp: Long = System.currentTimeMillis()
)