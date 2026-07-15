package com.fibbot.database.dao

import androidx.room.*
import com.fibbot.database.entity.CandleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CandleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandles(candles: List<CandleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandle(candle: CandleEntity)

    @Query("SELECT * FROM candles WHERE symbol = :symbol AND interval = :interval ORDER BY timestamp DESC LIMIT :limit")
    fun getCandlesBySymbolAndInterval(symbol: String, interval: String, limit: Int): Flow<List<CandleEntity>>

    @Query("SELECT * FROM candles WHERE symbol = :symbol AND interval = :interval AND timestamp >= :startTime ORDER BY timestamp DESC")
    suspend fun getCandlesInRange(symbol: String, interval: String, startTime: Long): List<CandleEntity>

    @Query("DELETE FROM candles WHERE symbol = :symbol AND interval = :interval AND timestamp < :retentionTime")
    suspend fun deleteOldCandles(symbol: String, interval: String, retentionTime: Long)

    @Query("DELETE FROM candles")
    suspend fun deleteAllCandles()

    @Query("SELECT COUNT(*) FROM candles")
    suspend fun getCandleCount(): Int
}