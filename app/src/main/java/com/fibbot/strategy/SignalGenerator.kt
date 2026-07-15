package com.fibbot.strategy

import com.fibbot.models.SignalType
import com.fibbot.models.IndicatorValues
import com.fibbot.models.FibonacciLevels
import com.fibbot.models.TradingSignal
import kotlin.math.abs

class SignalGenerator(
    private val fibonacciCalculator: FibonacciCalculator,
    private val technicalIndicators: TechnicalIndicators
) {
    fun generateSignal(
        symbol: String,
        candles: List<Map<String, Double>>,
        currentPrice: Double,
        timestamp: Long
    ): TradingSignal? {
        if (candles.size < 21) return null

        val closePrices = candles.mapNotNull { it["close"] }
        val ema9 = technicalIndicators.calculateEMA(closePrices, 9).lastOrNull() ?: return null
        val ema21 = technicalIndicators.calculateEMA(closePrices, 21).lastOrNull() ?: return null
        val rsiList = technicalIndicators.calculateRSI(closePrices, 14)
        val rsi = rsiList.lastOrNull() ?: return null
        val (macdLine, macdSignal, _) = technicalIndicators.calculateMACD(closePrices)
        val macd = macdLine.lastOrNull() ?: 0.0
        val macdSig = macdSignal.lastOrNull() ?: 0.0

        val swingHigh = fibonacciCalculator.findSwingHigh(candles)
        val swingLow = fibonacciCalculator.findSwingLow(candles)
        val fibLevels = fibonacciCalculator.calculateFibonacciLevels(swingHigh, swingLow)

        val indicatorValues = IndicatorValues(
            ema9 = ema9,
            ema21 = ema21,
            rsi14 = rsi,
            macdLine = macd,
            macdSignal = macdSig,
            macdHistogram = macd - macdSig
        )

        val fibonacciValues = FibonacciLevels(
            swingHigh = swingHigh,
            swingLow = swingLow,
            level0 = fibLevels["0.0"] ?: swingHigh,
            level236 = fibLevels["23.6"] ?: 0.0,
            level382 = fibLevels["38.2"] ?: 0.0,
            level5 = fibLevels["50.0"] ?: 0.0,
            level618 = fibLevels["61.8"] ?: 0.0,
            level786 = fibLevels["78.6"] ?: 0.0,
            level100 = fibLevels["100.0"] ?: swingLow
        )

        val (signalType, strength, confidence) = calculateSignalStrength(
            currentPrice, ema9, ema21, rsi, macd, macdSig, fibLevels
        )

        return TradingSignal(
            symbol = symbol,
            signalType = signalType,
            strength = strength,
            price = currentPrice,
            timestamp = timestamp,
            confidence = confidence,
            indicators = indicatorValues,
            fibonacci = fibonacciValues
        )
    }

    private fun calculateSignalStrength(
        price: Double,
        ema9: Double,
        ema21: Double,
        rsi: Double,
        macd: Double,
        macdSignal: Double,
        fibLevels: Map<String, Double>
    ): Triple<SignalType, Float, Float> {
        var buyScore = 0
        var sellScore = 0
        var confidence = 0f

        // EMA signals
        if (price > ema9 && ema9 > ema21) {
            buyScore += 2
        } else if (price < ema9 && ema9 < ema21) {
            sellScore += 2
        }

        // RSI signals
        when {
            rsi < 30 -> buyScore += 2
            rsi > 70 -> sellScore += 2
            rsi in 40.0..60.0 -> buyScore += 1
        }

        // MACD signals
        if (macd > macdSignal) {
            buyScore += 1
        } else if (macd < macdSignal) {
            sellScore += 1
        }

        // Fibonacci signals
        val fib618 = fibLevels["61.8"] ?: 0.0
        val fib382 = fibLevels["38.2"] ?: 0.0
        if (abs(price - fib618) / price < 0.01) buyScore += 1
        if (abs(price - fib382) / price < 0.01) sellScore += 1

        val totalScore = maxOf(buyScore, sellScore)
        confidence = when (totalScore) {
            in 0..2 -> 0.3f
            in 3..4 -> 0.6f
            else -> 0.9f
        }

        val signalType = when {
            buyScore > sellScore -> SignalType.BUY
            sellScore > buyScore -> SignalType.SELL
            else -> SignalType.HOLD
        }

        val strength = (totalScore / 6.0).toFloat().coerceIn(0f, 1f)
        return Triple(signalType, strength, confidence)
    }
}