package com.fibbot.di

import android.content.Context
import com.fibbot.api.BinanceRestClient
import com.fibbot.api.BinanceWebSocketManager
import com.fibbot.database.FibBotDatabase
import com.fibbot.repository.CandleRepository
import com.fibbot.repository.TradeRepository
import com.fibbot.repository.PriceRepository
import com.fibbot.strategy.FibonacciCalculator
import com.fibbot.strategy.TechnicalIndicators
import com.fibbot.strategy.SignalGenerator
import com.fibbot.strategy.RiskManager
import com.fibbot.viewmodel.TradingViewModel
import com.fibbot.viewmodel.ChartViewModel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object DependencyInjection {
    private lateinit var context: Context
    private lateinit var database: FibBotDatabase
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var gson: Gson
    private lateinit var retrofit: Retrofit

    fun init(appContext: Context) {
        context = appContext
        database = FibBotDatabase.getInstance(context)
        gson = Gson()
        okHttpClient = buildOkHttpClient()
        retrofit = buildRetrofit()
    }

    private fun buildOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.binance.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun provideBinanceRestClient(): BinanceRestClient {
        return retrofit.create(BinanceRestClient::class.java)
    }

    fun provideBinanceWebSocketManager(): BinanceWebSocketManager {
        return BinanceWebSocketManager(okHttpClient, gson)
    }

    fun provideCandleRepository(): CandleRepository {
        return CandleRepository(
            database.candleDao(),
            provideBinanceRestClient()
        )
    }

    fun provideTradeRepository(): TradeRepository {
        return TradeRepository(database.tradeDao())
    }

    fun providePriceRepository(): PriceRepository {
        return PriceRepository(
            database.priceDao(),
            provideBinanceRestClient()
        )
    }

    fun provideFibonacciCalculator(): FibonacciCalculator {
        return FibonacciCalculator()
    }

    fun provideTechnicalIndicators(): TechnicalIndicators {
        return TechnicalIndicators()
    }

    fun provideSignalGenerator(): SignalGenerator {
        return SignalGenerator(
            provideFibonacciCalculator(),
            provideTechnicalIndicators()
        )
    }

    fun provideRiskManager(): RiskManager {
        return RiskManager(
            accountBalance = 1000.0,
            riskPercentPerTrade = 0.02,
            maxDailyLossPercent = 0.05
        )
    }

    fun provideTradingViewModel(): TradingViewModel {
        return TradingViewModel(
            provideCandleRepository(),
            provideTradeRepository(),
            providePriceRepository(),
            provideSignalGenerator(),
            provideRiskManager()
        )
    }

    fun provideChartViewModel(): ChartViewModel {
        return ChartViewModel(
            provideCandleRepository(),
            provideTechnicalIndicators()
        )
    }
}