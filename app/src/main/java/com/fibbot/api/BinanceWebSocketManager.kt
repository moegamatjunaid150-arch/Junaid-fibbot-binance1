package com.fibbot.api

import com.fibbot.models.BinanceWebSocketKline
import com.fibbot.models.BinanceWebSocketTicker
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber

class BinanceWebSocketManager(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) {
    private var webSocket: WebSocket? = null
    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private var reconnectJob: Job? = null
    private var isManuallyClosed = false

    private val _klineFlow = MutableSharedFlow<BinanceWebSocketKline>()
    val klineFlow: SharedFlow<BinanceWebSocketKline> = _klineFlow

    private val _tickerFlow = MutableSharedFlow<BinanceWebSocketTicker>()
    val tickerFlow: SharedFlow<BinanceWebSocketTicker> = _tickerFlow

    fun subscribeToKlines(symbols: List<String>, interval: String = "1m") {
        if (symbols.isEmpty()) return
        val streams = symbols.map { "${it.lowercase()}@kline_$interval" }.joinToString("/")
        val url = "wss://stream.binance.com:9443/stream?streams=$streams"
        Timber.tag("WebSocket").d("Subscribing to klines: $url")
        connect(url)
    }

    fun subscribeToTickers(symbols: List<String>) {
        if (symbols.isEmpty()) return
        val streams = symbols.map { "${it.lowercase()}@ticker" }.joinToString("/")
        val url = "wss://stream.binance.com:9443/stream?streams=$streams"
        Timber.tag("WebSocket").d("Subscribing to tickers: $url")
        connect(url)
    }

    private fun connect(url: String) {
        if (webSocket != null) {
            disconnect()
        }

        isManuallyClosed = false
        val request = Request.Builder().url(url).build()
        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                Timber.tag("WebSocket").d("WebSocket connected")
                cancelReconnect()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val data = gson.fromJson(text, Map::class.java)
                    val stream = data["stream"] as? String ?: return
                    val message = data["data"] as? Map<*, *> ?: return

                    when {
                        stream.contains("@kline_") -> {
                            val klineData = gson.fromJson(
                                gson.toJson(message),
                                BinanceWebSocketKline::class.java
                            )
                            scope.launch { _klineFlow.emit(klineData) }
                        }
                        stream.contains("@ticker") -> {
                            val tickerData = gson.fromJson(
                                gson.toJson(message),
                                BinanceWebSocketTicker::class.java
                            )
                            scope.launch { _tickerFlow.emit(tickerData) }
                        }
                    }
                } catch (e: Exception) {
                    Timber.tag("WebSocket").e(e, "Error parsing message")
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Timber.tag("WebSocket").d("WebSocket closing: $reason")
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Timber.tag("WebSocket").d("WebSocket closed: $reason")
                if (!isManuallyClosed) {
                    scheduleReconnect()
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                Timber.tag("WebSocket").e(t, "WebSocket error")
                if (!isManuallyClosed) {
                    scheduleReconnect()
                }
            }
        })
    }

    private fun scheduleReconnect() {
        reconnectJob?.cancel()
        reconnectJob = scope.launch {
            delay(5000)
            val currentUrl = webSocket?.request()?.url.toString()
            if (currentUrl.isNotEmpty() && !isManuallyClosed) {
                connect(currentUrl)
            }
        }
    }

    private fun cancelReconnect() {
        reconnectJob?.cancel()
        reconnectJob = null
    }

    fun disconnect() {
        isManuallyClosed = true
        webSocket?.close(1000, "Client disconnect")
        cancelReconnect()
    }

    fun cleanup() {
        disconnect()
        scope.cancel()
    }
}