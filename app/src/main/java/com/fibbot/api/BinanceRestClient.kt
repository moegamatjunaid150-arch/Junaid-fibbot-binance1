package com.fibbot.api

import com.fibbot.models.BinanceKline
import com.fibbot.models.BinanceTickerPrice
import com.fibbot.models.BinanceExchangeInfo
import retrofit2.http.*

interface BinanceRestClient {
    @GET("/api/v3/ticker/price")
    suspend fun getTickerPrice(@Query("symbol") symbol: String): BinanceTickerPrice

    @GET("/api/v3/ticker/24hr")
    suspend fun get24hrTickerPrice(@Query("symbol") symbol: String): Map<String, Any>

    @GET("/api/v3/klines")
    suspend fun getKlines(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("limit") limit: Int = 500
    ): List<List<Any>>

    @GET("/api/v3/exchangeInfo")
    suspend fun getExchangeInfo(): BinanceExchangeInfo

    @GET("/api/v3/account")
    suspend fun getAccountInfo(
        @Header("X-MBX-APIKEY") apiKey: String,
        @Query("timestamp") timestamp: Long,
        @Query("signature") signature: String
    ): Map<String, Any>

    @GET("/api/v3/openOrders")
    suspend fun getOpenOrders(
        @Header("X-MBX-APIKEY") apiKey: String,
        @Query("timestamp") timestamp: Long,
        @Query("signature") signature: String
    ): List<Map<String, Any>>
}