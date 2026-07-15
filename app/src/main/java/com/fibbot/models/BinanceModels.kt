package com.fibbot.models

import com.google.gson.annotations.SerializedName

// REST API Models
data class BinanceTickerPrice(
    val symbol: String,
    val price: String
)

data class BinanceKline(
    @SerializedName("0") val openTime: Long,
    @SerializedName("1") val open: String,
    @SerializedName("2") val high: String,
    @SerializedName("3") val low: String,
    @SerializedName("4") val close: String,
    @SerializedName("5") val volume: String,
    @SerializedName("6") val closeTime: Long,
    @SerializedName("7") val quoteAssetVolume: String,
    @SerializedName("8") val numberOfTrades: Long
)

data class BinanceExchangeInfo(
    val symbols: List<Symbol>
) {
    data class Symbol(
        val symbol: String,
        val baseAsset: String,
        val quoteAsset: String,
        val status: String
    )
}

// WebSocket Models
data class BinanceWebSocketKline(
    val e: String,
    val E: Long,
    val s: String,
    val k: KlineData
) {
    data class KlineData(
        val t: Long,
        val T: Long,
        val s: String,
        val i: String,
        val f: Long,
        val L: Long,
        val o: String,
        val c: String,
        val h: String,
        val l: String,
        val v: String,
        val n: Long,
        val x: Boolean,
        val q: String,
        val V: String,
        val Q: String
    )
}

data class BinanceWebSocketTicker(
    val e: String,
    val E: Long,
    val s: String,
    val c: String,
    val p: String,
    val P: String,
    val w: String
)