package com.fibbot.repository

import com.fibbot.api.BinanceRestClient
import com.fibbot.database.dao.PriceDao
import com.fibbot.database.entity.PriceCacheEntity
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class PriceRepository(
    private val priceDao: PriceDao,
    private val binanceRestClient: BinanceRestClient
) {
    fun getPriceBySymbol(symbol: String): Flow<PriceCacheEntity?> {
        return priceDao.getPriceBySymbol(symbol)
    }

    fun getAllPrices(): Flow<List<PriceCacheEntity>> {
        return priceDao.getAllPrices()
    }

    fun getPricesBySymbols(symbols: List<String>): Flow<List<PriceCacheEntity>> {
        return priceDao.getPricesBySymbols(symbols)
    }

    suspend fun fetchAndCachePrice(symbol: String) {
        try {
            val tickerPrice = binanceRestClient.getTickerPrice(symbol)
            val priceEntity = PriceCacheEntity(
                symbol = symbol,
                price = tickerPrice.price.toDouble(),
                timestamp = System.currentTimeMillis()
            )
            priceDao.insertPrice(priceEntity)
            Timber.d("Cached price for $symbol: ${tickerPrice.price}")
        } catch (e: Exception) {
            Timber.e(e, "Error fetching price for $symbol")
        }
    }

    suspend fun fetchAndCachePrices(symbols: List<String>) {
        symbols.forEach { symbol ->
            fetchAndCachePrice(symbol)
        }
    }

    suspend fun deleteAllPrices() {
        priceDao.deleteAllPrices()
    }
}