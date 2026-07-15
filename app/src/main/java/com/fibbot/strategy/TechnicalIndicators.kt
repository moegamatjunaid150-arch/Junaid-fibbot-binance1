package com.fibbot.strategy

import kotlin.math.ln

class TechnicalIndicators {
    fun calculateEMA(prices: List<Double>, period: Int): List<Double> {
        if (prices.isEmpty()) return emptyList()
        val k = 2.0 / (period + 1)
        val ema = mutableListOf<Double>()
        var currentEma = prices.first()
        ema.add(currentEma)

        for (i in 1 until prices.size) {
            currentEma = prices[i] * k + currentEma * (1 - k)
            ema.add(currentEma)
        }
        return ema
    }

    fun calculateRSI(prices: List<Double>, period: Int = 14): List<Double> {
        if (prices.size < period + 1) return emptyList()
        val rsi = mutableListOf<Double>()
        var gains = 0.0
        var losses = 0.0

        for (i in 1..period) {
            val diff = prices[i] - prices[i - 1]
            if (diff > 0) gains += diff else losses += -diff
        }

        var avgGain = gains / period
        var avgLoss = losses / period

        for (i in period until prices.size) {
            val diff = prices[i] - prices[i - 1]
            if (diff > 0) {
                avgGain = (avgGain * (period - 1) + diff) / period
                avgLoss = avgLoss * (period - 1) / period
            } else {
                avgGain = avgGain * (period - 1) / period
                avgLoss = (avgLoss * (period - 1) + (-diff)) / period
            }

            val rs = if (avgLoss == 0.0) 100.0 else avgGain / avgLoss
            val rsiValue = 100.0 - (100.0 / (1.0 + rs))
            rsi.add(rsiValue)
        }
        return rsi
    }

    fun calculateMACD(
        prices: List<Double>,
        fastPeriod: Int = 12,
        slowPeriod: Int = 26,
        signalPeriod: Int = 9
    ): Triple<List<Double>, List<Double>, List<Double>> {
        val fastEMA = calculateEMA(prices, fastPeriod)
        val slowEMA = calculateEMA(prices, slowPeriod)
        val minSize = minOf(fastEMA.size, slowEMA.size)

        val macdLine = (0 until minSize).map { i ->
            fastEMA[fastEMA.size - minSize + i] - slowEMA[slowEMA.size - minSize + i]
        }

        val signal = calculateEMA(macdLine, signalPeriod)
        val histogram = (0 until minOf(macdLine.size, signal.size)).map { i ->
            macdLine[macdLine.size - signal.size + i] - signal[i]
        }

        return Triple(macdLine, signal, histogram)
    }

    fun calculateATR(candles: List<Map<String, Double>>, period: Int = 14): List<Double> {
        if (candles.size < 2) return emptyList()
        val trueRanges = mutableListOf<Double>()

        for (i in 1 until candles.size) {
            val high = candles[i]["high"] ?: 0.0
            val low = candles[i]["low"] ?: 0.0
            val prevClose = candles[i - 1]["close"] ?: 0.0

            val tr = maxOf(
                high - low,
                kotlin.math.abs(high - prevClose),
                kotlin.math.abs(low - prevClose)
            )
            trueRanges.add(tr)
        }

        val atr = mutableListOf<Double>()
        var avgTr = trueRanges.take(period).average()
        atr.add(avgTr)

        for (i in period until trueRanges.size) {
            avgTr = (avgTr * (period - 1) + trueRanges[i]) / period
            atr.add(avgTr)
        }
        return atr
    }

    fun calculateBollingerBands(
        prices: List<Double>,
        period: Int = 20,
        stdDevs: Double = 2.0
    ): Triple<List<Double>, List<Double>, List<Double>> {
        val sma = mutableListOf<Double>()
        val upperBand = mutableListOf<Double>()
        val lowerBand = mutableListOf<Double>()

        for (i in period - 1 until prices.size) {
            val subset = prices.subList(i - period + 1, i + 1)
            val mean = subset.average()
            val variance = subset.map { (it - mean) * (it - mean) }.average()
            val stdDev = kotlin.math.sqrt(variance)

            sma.add(mean)
            upperBand.add(mean + (stdDevs * stdDev))
            lowerBand.add(mean - (stdDevs * stdDev))
        }
        return Triple(sma, upperBand, lowerBand)
    }
}