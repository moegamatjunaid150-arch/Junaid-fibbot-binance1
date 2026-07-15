package com.fibbot.repository

import com.fibbot.database.dao.TradeDao
import com.fibbot.database.entity.TradeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class TradeRepository(private val tradeDao: TradeDao) {
    fun getTradesBySymbol(symbol: String): Flow<List<TradeEntity>> {
        return tradeDao.getTradesBySymbol(symbol)
    }

    fun getAllTrades(): Flow<List<TradeEntity>> {
        return tradeDao.getAllTrades()
    }

    fun getOpenTrades(): Flow<List<TradeEntity>> {
        return tradeDao.getOpenTrades()
    }

    fun getClosedTrades(limit: Int = 100): Flow<List<TradeEntity>> {
        return tradeDao.getClosedTrades(limit)
    }

    suspend fun insertTrade(trade: TradeEntity): Long {
        return tradeDao.insertTrade(trade)
    }

    suspend fun updateTrade(trade: TradeEntity) {
        tradeDao.updateTrade(trade)
    }

    suspend fun getTradeById(tradeId: Long): TradeEntity? {
        return tradeDao.getTradeById(tradeId)
    }

    suspend fun getClosedTradesCount(): Int {
        return tradeDao.getClosedTradesCount()
    }

    suspend fun getTotalProfitLoss(): Double {
        return tradeDao.getTotalProfitLoss() ?: 0.0
    }

    suspend fun getWinRate(): Float {
        val closedCount = getClosedTradesCount()
        if (closedCount == 0) return 0f

        val winningTrades = getAllTrades()
            .first()
            .count { it.status == "CLOSED" && it.profitLoss > 0 }
        return if (closedCount > 0) (winningTrades.toFloat() / closedCount) * 100 else 0f
    }

    suspend fun deleteAllTrades() {
        tradeDao.deleteAllTrades()
    }
}