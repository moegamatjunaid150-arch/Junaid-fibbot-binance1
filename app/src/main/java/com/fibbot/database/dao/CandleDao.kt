package com.fibbot.database.dao

import androidx.room.*
import com.fibbot.models.CandleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CandleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandle(candle: CandleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandles(candles: List<CandleEntity>)

    @Query("SELECT * FROM candles WHERE symbol = :symbol AND interval = :interval ORDER BY timestamp DESC LIMIT :limit")
    fun getCandlesBySymbolAndInterval(symbol: String, interval: String, limit: Int = 1000): Flow<List<CandleEntity>>

    @Query("SELECT * FROM candles WHERE symbol = :symbol AND interval = :interval AND timestamp >= :startTime ORDER BY timestamp ASC")
    suspend fun getCandlesInRange(symbol: String, interval: String, startTime: Long): List<CandleEntity>

    @Query("DELETE FROM candles WHERE symbol = :symbol AND interval = :interval AND timestamp < :retentionTime")
    suspend fun deleteOldCandles(symbol: String, interval: String, retentionTime: Long)

    @Query("DELETE FROM candles")
    suspend fun deleteAllCandles()
}