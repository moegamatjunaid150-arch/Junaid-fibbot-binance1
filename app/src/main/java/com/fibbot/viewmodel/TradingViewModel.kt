package com.fibbot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fibbot.repository.CandleRepository
import com.fibbot.repository.PriceRepository
import com.fibbot.repository.TradeRepository
import com.fibbot.security.ApiKeyManager
import com.fibbot.strategy.SignalGenerator
import com.fibbot.strategy.RiskManager
import com.fibbot.database.entity.TradeEntity
import com.fibbot.models.SignalType
import com.fibbot.models.TradingSignal
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class TradingViewModel(
    private val candleRepository: CandleRepository,
    private val tradeRepository: TradeRepository,
    private val priceRepository: PriceRepository,
    private val signalGenerator: SignalGenerator,
    private val riskManager: RiskManager,
    private val apiKeyManager: ApiKeyManager
) : ViewModel() {

    private val _tradingSignals = MutableSharedFlow<TradingSignal>()
    val tradingSignals: SharedFlow<TradingSignal> = _tradingSignals.asSharedFlow()

    private val _currentTrades = MutableStateFlow<List<TradeEntity>>(emptyList())
    val currentTrades: StateFlow<List<TradeEntity>> = _currentTrades.asStateFlow()

    private val _totalProfitLoss = MutableStateFlow(0.0)
    val totalProfitLoss: StateFlow<Double> = _totalProfitLoss.asStateFlow()

    private val _isTrading = MutableStateFlow(false)
    val isTrading: StateFlow<Boolean> = _isTrading.asStateFlow()

    private val _hasApiKeys = MutableStateFlow(false)
    val hasApiKeys: StateFlow<Boolean> = _hasApiKeys.asStateFlow()

    init {
        _hasApiKeys.value = apiKeyManager.hasApiKeys()

        viewModelScope.launch {
            tradeRepository.getOpenTrades().collect { trades ->
                _currentTrades.value = trades
            }
        }

        viewModelScope.launch {
            _totalProfitLoss.value = tradeRepository.getTotalProfitLoss()
        }
    }

    fun analyzeAndTrade(symbol: String, interval: String = "1m") {
        viewModelScope.launch {
            try {
                _isTrading.value = true
                candleRepository.fetchAndCacheCandles(symbol, interval)
                priceRepository.fetchAndCachePrice(symbol)

                val (candles, priceEntity) = coroutineScope {
                    val candlesDeferred = async {
                        candleRepository.getCandlesBySymbolAndInterval(symbol, interval).firstOrNull()
                    }
                    val priceDeferred = async {
                        priceRepository.getPriceBySymbol(symbol).firstOrNull()
                    }

                    candlesDeferred.await() to priceDeferred.await()
                }

                if (!candles.isNullOrEmpty() && priceEntity != null) {
                    val candleMap = candles.map { candle ->
                        mapOf(
                            "open" to candle.open,
                            "high" to candle.high,
                            "low" to candle.low,
                            "close" to candle.close,
                            "volume" to candle.volume
                        )
                    }

                    val signal = signalGenerator.generateSignal(
                        symbol,
                        candleMap,
                        priceEntity.price,
                        System.currentTimeMillis()
                    )

                    if (signal != null) {
                        _tradingSignals.emit(signal)
                        when (signal.signalType) {
                            SignalType.BUY, SignalType.SELL -> executeTrade(signal, priceEntity.price)
                            SignalType.HOLD -> Unit
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error analyzing trades")
            } finally {
                _isTrading.value = false
            }
        }
    }

    private suspend fun executeTrade(signal: TradingSignal, currentPrice: Double) {
        if (!riskManager.canOpenTrade(_totalProfitLoss.value)) {
            Timber.w("Daily loss limit reached")
            return
        }

        val stopLoss = riskManager.calculateStopLoss(currentPrice)
        val takeProfit = riskManager.calculateTakeProfit(currentPrice, 2.0, stopLoss)
        val positionSize = riskManager.calculatePositionSize(currentPrice, stopLoss)

        if (!riskManager.validateTradeSize(positionSize)) {
            Timber.w("Invalid trade size: $positionSize")
            return
        }

        val side = when (signal.signalType) {
            SignalType.BUY -> "BUY"
            SignalType.SELL -> "SELL"
            else -> return
        }

        val trade = TradeEntity(
            id = 0,
            symbol = signal.symbol,
            side = side,
            entryPrice = currentPrice,
            quantity = positionSize,
            stopLoss = stopLoss,
            takeProfit = takeProfit,
            entryTime = System.currentTimeMillis(),
            status = "OPEN",
            isPaperTrade = !apiKeyManager.hasApiKeys(),
            profitLoss = 0.0,
            profitLossPercent = 0.0
        )

        val tradeId = tradeRepository.insertTrade(trade)
        Timber.d("Trade opened: $tradeId for ${signal.symbol} (side=$side, paper=${trade.isPaperTrade})")
    }

    fun closeOpenTrades() {
        viewModelScope.launch {
            try {
                val prices = priceRepository.getAllPrices().firstOrNull() ?: return@launch
                _currentTrades.value.forEach { trade ->
                    val price = prices.find { it.symbol == trade.symbol }?.price
                    if (price != null) {
                        val (pl, plPercent) = riskManager.calculateProfitLoss(
                            trade.entryPrice,
                            price,
                            trade.quantity,
                            trade.side
                        )

                        val closedTrade = trade.copy(
                            status = "CLOSED",
                            exitPrice = price,
                            exitTime = System.currentTimeMillis(),
                            profitLoss = pl,
                            profitLossPercent = plPercent
                        )

                        tradeRepository.updateTrade(closedTrade)
                        _totalProfitLoss.value += pl
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error closing trades")
            }
        }
    }

    fun refreshApiKeyStatus() {
        _hasApiKeys.value = apiKeyManager.hasApiKeys()
    }
}