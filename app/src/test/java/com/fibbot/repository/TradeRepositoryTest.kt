package com.fibbot.repository

import com.fibbot.database.dao.TradeDao
import com.fibbot.database.entity.TradeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class TradeRepositoryTest {
    @Test
    fun `getWinRate returns winning percentage for closed trades`() = runBlocking {
        val repository = TradeRepository(
            FakeTradeDao(
                listOf(
                    trade(id = 1, status = "CLOSED", profitLoss = 10.0),
                    trade(id = 2, status = "CLOSED", profitLoss = -5.0),
                    trade(id = 3, status = "OPEN", profitLoss = 20.0)
                )
            )
        )

        assertEquals(50f, repository.getWinRate())
    }

    @Test
    fun `getWinRate returns zero when there are no closed trades`() = runBlocking {
        val repository = TradeRepository(
            FakeTradeDao(
                listOf(
                    trade(id = 1, status = "OPEN", profitLoss = 10.0)
                )
            )
        )

        assertEquals(0f, repository.getWinRate())
    }

    private fun trade(id: Long, status: String, profitLoss: Double) = TradeEntity(
        id = id,
        symbol = "BTCUSDT",
        side = "BUY",
        entryPrice = 100.0,
        quantity = 1.0,
        stopLoss = 95.0,
        takeProfit = 110.0,
        entryTime = 1L,
        status = status,
        profitLoss = profitLoss,
        profitLossPercent = 0.0
    )

    private class FakeTradeDao(
        private val trades: List<TradeEntity>
    ) : TradeDao {
        override suspend fun insertTrade(trade: TradeEntity): Long = trade.id

        override suspend fun updateTrade(trade: TradeEntity) = Unit

        override suspend fun deleteTrade(trade: TradeEntity) = Unit

        override fun getTradesBySymbol(symbol: String): Flow<List<TradeEntity>> =
            flowOf(trades.filter { it.symbol == symbol })

        override fun getAllTrades(): Flow<List<TradeEntity>> = flowOf(trades)

        override fun getOpenTrades(): Flow<List<TradeEntity>> =
            flowOf(trades.filter { it.status == "OPEN" })

        override fun getClosedTrades(limit: Int): Flow<List<TradeEntity>> =
            flowOf(trades.filter { it.status == "CLOSED" }.take(limit))

        override suspend fun getTradeById(tradeId: Long): TradeEntity? =
            trades.firstOrNull { it.id == tradeId }

        override suspend fun getClosedTradesCount(): Int =
            trades.count { it.status == "CLOSED" }

        override suspend fun getTotalProfitLoss(): Double? =
            trades.filter { it.status == "CLOSED" }.sumOf { it.profitLoss }

        override suspend fun getWinningTradesCount(): Int =
            trades.count { it.status == "CLOSED" && it.profitLoss > 0 }

        override suspend fun deleteAllTrades() = Unit
    }
}
