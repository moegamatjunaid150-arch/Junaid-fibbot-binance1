package com.fibbot.database.dao

import androidx.room.*
import com.fibbot.models.PriceCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrice(price: PriceCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrices(prices: List<PriceCacheEntity>)

    @Query("SELECT * FROM price_cache WHERE symbol = :symbol")
    fun getPriceBySymbol(symbol: String): Flow<PriceCacheEntity?>

    @Query("SELECT * FROM price_cache ORDER BY symbol ASC")
    fun getAllPrices(): Flow<List<PriceCacheEntity>>

    @Query("SELECT * FROM price_cache WHERE symbol IN (:symbols)")
    fun getPricesBySymbols(symbols: List<String>): Flow<List<PriceCacheEntity>>

    @Query("DELETE FROM price_cache")
    suspend fun deleteAllPrices()
}