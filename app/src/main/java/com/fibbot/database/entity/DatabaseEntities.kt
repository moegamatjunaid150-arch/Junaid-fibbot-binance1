package com.fibbot.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candles")
data class CandleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val symbol: String,
    val interval: String,
    val timestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double
)

@Entity(tableName = "trades")
data class TradeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val symbol: String,
    val side: String,
    val entryPrice: Double,
    val quantity: Double,
    val stopLoss: Double,
    val takeProfit: Double,
    val entryTime: Long,
    val exitTime: Long? = null,
    val exitPrice: Double? = null,
    val status: String,
    val isPaperTrade: Boolean = true,
    val profitLoss: Double = 0.0,
    val profitLossPercent: Double = 0.0
)

@Entity(tableName = "price_cache")
data class PriceCacheEntity(
    @PrimaryKey
    val symbol: String,
    val price: Double,
    val timestamp: Long = System.currentTimeMillis()
)