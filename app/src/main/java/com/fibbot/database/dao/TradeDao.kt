package com.fibbot.database.dao

import androidx.room.*
import com.fibbot.models.TradeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrade(trade: TradeEntity): Long

    @Update
    suspend fun updateTrade(trade: TradeEntity)

    @Query("SELECT * FROM trades WHERE symbol = :symbol ORDER BY entryTime DESC")
    fun getTradesBySymbol(symbol: String): Flow<List<TradeEntity>>

    @Query("SELECT * FROM trades ORDER BY entryTime DESC")
    fun getAllTrades(): Flow<List<TradeEntity>>

    @Query("SELECT * FROM trades WHERE status = 'OPEN' ORDER BY entryTime DESC")
    fun getOpenTrades(): Flow<List<TradeEntity>>

    @Query("SELECT * FROM trades WHERE status = 'CLOSED' ORDER BY exitTime DESC LIMIT :limit")
    fun getClosedTrades(limit: Int = 100): Flow<List<TradeEntity>>

    @Query("SELECT * FROM trades WHERE id = :tradeId")
    suspend fun getTradeById(tradeId: Long): TradeEntity?

    @Query("SELECT COUNT(*) FROM trades WHERE isPaperTrade = 1 AND status = 'CLOSED'")
    suspend fun getClosedTradesCount(): Int

    @Query("SELECT SUM(profitLoss) FROM trades WHERE isPaperTrade = 1 AND status = 'CLOSED'")
    suspend fun getTotalProfitLoss(): Double?

    @Query("DELETE FROM trades")
    suspend fun deleteAllTrades()
}