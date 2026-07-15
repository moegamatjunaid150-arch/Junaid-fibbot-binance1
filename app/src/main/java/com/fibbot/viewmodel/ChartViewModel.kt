package com.fibbot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fibbot.repository.CandleRepository
import com.fibbot.strategy.TechnicalIndicators
import com.fibbot.models.ChartData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ChartViewModel(
    private val candleRepository: CandleRepository,
    private val technicalIndicators: TechnicalIndicators
) : ViewModel() {

    private val _chartData = MutableStateFlow<ChartData?>(null)
    val chartData: StateFlow<ChartData?> = _chartData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadChartData(symbol: String, interval: String = "1m") {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                candleRepository.getCandlesBySymbolAndInterval(symbol, interval).collect { candles ->
                    if (candles.isNotEmpty()) {
                        val closePrices = candles.map { it.close }
                        val ema9 = technicalIndicators.calculateEMA(closePrices, 9)
                        val ema21 = technicalIndicators.calculateEMA(closePrices, 21)
                        val rsi = technicalIndicators.calculateRSI(closePrices, 14)
                        val (macdLine, macdSignal, histogram) = technicalIndicators.calculateMACD(closePrices)
                        val atr = technicalIndicators.calculateATR(
                            candles.map { mapOf(
                                "high" to it.high,
                                "low" to it.low,
                                "close" to it.close
                            )}
                        )
                        val (sma, upperBand, lowerBand) = technicalIndicators.calculateBollingerBands(closePrices)

                        val data = ChartData(
                            candles = candles,
                            ema9 = ema9,
                            ema21 = ema21,
                            rsi14 = rsi,
                            macdLine = macdLine,
                            macdSignal = macdSignal,
                            macdHistogram = histogram,
                            atr = atr,
                            bbSma = sma,
                            bbUpper = upperBand,
                            bbLower = lowerBand
                        )

                        _chartData.value = data
                        Timber.d("Chart data loaded for $symbol")
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error loading chart data")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh(symbol: String, interval: String = "1m") {
        loadChartData(symbol, interval)
    }
}