package com.fibbot.repository

import com.fibbot.api.BinanceRestClient
import com.fibbot.database.dao.CandleDao
import com.fibbot.database.entity.CandleEntity
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class CandleRepository(
    private val candleDao: CandleDao,
    private val binanceRestClient: BinanceRestClient
) {
    fun getCandlesBySymbolAndInterval(
        symbol: String,
        interval: String,
        limit: Int = 1000
    ): Flow<List<CandleEntity>> {
        return candleDao.getCandlesBySymbolAndInterval(symbol, interval, limit)
    }

    suspend fun fetchAndCacheCandles(
        symbol: String,
        interval: String,
        limit: Int = 500
    ) {
        try {
            val klines = binanceRestClient.getKlines(symbol, interval, limit)
            val candles = klines.map { kline ->
                CandleEntity(
                    symbol = symbol,
                    interval = interval,
                    timestamp = (kline[0] as Number).toLong(),
                    open = (kline[1] as String).toDouble(),
                    high = (kline[2] as String).toDouble(),
                    low = (kline[3] as String).toDouble(),
                    close = (kline[4] as String).toDouble(),
                    volume = (kline[7] as String).toDouble()
                )
            }
            candleDao.insertCandles(candles)
            Timber.d("Cached ${candles.size} candles for $symbol")
        } catch (e: Exception) {
            Timber.e(e, "Error fetching candles for $symbol")
        }
    }

    suspend fun getCandlesInRange(
        symbol: String,
        interval: String,
        startTime: Long
    ): List<CandleEntity> {
        return candleDao.getCandlesInRange(symbol, interval, startTime)
    }

    suspend fun deleteOldCandles(
        symbol: String,
        interval: String,
        retentionTime: Long
    ) {
        candleDao.deleteOldCandles(symbol, interval, retentionTime)
    }

    suspend fun deleteAllCandles() {
        candleDao.deleteAllCandles()
    }
}