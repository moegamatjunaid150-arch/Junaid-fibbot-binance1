package com.fibbot.database.dao

import androidx.room.*
import com.fibbot.database.entity.TradeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrade(trade: TradeEntity): Long

    @Update
    suspend fun updateTrade(trade: TradeEntity)

    @Delete
    suspend fun deleteTrade(trade: TradeEntity)

    @Query("SELECT * FROM trades WHERE symbol = :symbol ORDER BY entryTime DESC")
    fun getTradesBySymbol(symbol: String): Flow<List<TradeEntity>>

    @Query("SELECT * FROM trades ORDER BY entryTime DESC")
    fun getAllTrades(): Flow<List<TradeEntity>>

    @Query("SELECT * FROM trades WHERE status = 'OPEN' ORDER BY entryTime DESC")
    fun getOpenTrades(): Flow<List<TradeEntity>>

    @Query("SELECT * FROM trades WHERE status = 'CLOSED' ORDER BY entryTime DESC LIMIT :limit")
    fun getClosedTrades(limit: Int = 100): Flow<List<TradeEntity>>

    @Query("SELECT * FROM trades WHERE id = :tradeId")
    suspend fun getTradeById(tradeId: Long): TradeEntity?

    @Query("SELECT COUNT(*) FROM trades WHERE status = 'CLOSED'")
    suspend fun getClosedTradesCount(): Int

    @Query("SELECT SUM(profitLoss) FROM trades WHERE status = 'CLOSED'")
    suspend fun getTotalProfitLoss(): Double?

    @Query("SELECT COUNT(*) FROM trades WHERE status = 'CLOSED' AND profitLoss > 0")
    suspend fun getWinningTradesCount(): Int

    @Query("DELETE FROM trades")
    suspend fun deleteAllTrades()
}