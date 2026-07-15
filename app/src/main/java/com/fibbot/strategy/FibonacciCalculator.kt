package com.fibbot.strategy

import kotlin.math.abs

class FibonacciCalculator {
    fun calculateFibonacciLevels(swingHigh: Double, swingLow: Double): Map<String, Double> {
        val diff = abs(swingHigh - swingLow)
        return mapOf(
            "0.0" to swingHigh,
            "23.6" to swingHigh - (diff * 0.236),
            "38.2" to swingHigh - (diff * 0.382),
            "50.0" to swingHigh - (diff * 0.5),
            "61.8" to swingHigh - (diff * 0.618),
            "78.6" to swingHigh - (diff * 0.786),
            "100.0" to swingLow
        )
    }

    fun isNearFibonacciLevel(price: Double, level: Double, tolerance: Double = 0.005): Boolean {
        val percent = abs(price - level) / level
        return percent <= tolerance
    }

    fun getClosestFibLevel(price: Double, levels: Map<String, Double>): Pair<String, Double> {
        return levels.minByOrNull { (_, level) -> abs(price - level) } ?: "0.0" to price
    }

    fun findSwingHigh(candles: List<Map<String, Double>>, period: Int = 20): Double {
        return candles.takeLast(period).maxOfOrNull { it["high"] ?: 0.0 } ?: 0.0
    }

    fun findSwingLow(candles: List<Map<String, Double>>, period: Int = 20): Double {
        return candles.takeLast(period).minOfOrNull { it["low"] ?: 0.0 } ?: 0.0
    }
}