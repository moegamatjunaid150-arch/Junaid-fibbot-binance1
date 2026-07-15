package com.fibbot.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fibbot.database.dao.CandleDao
import com.fibbot.database.dao.TradeDao
import com.fibbot.database.dao.PriceDao
import com.fibbot.models.CandleEntity
import com.fibbot.models.TradeEntity
import com.fibbot.models.PriceCacheEntity

@Database(
    entities = [CandleEntity::class, TradeEntity::class, PriceCacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FibBotDatabase : RoomDatabase() {
    abstract fun candleDao(): CandleDao
    abstract fun tradeDao(): TradeDao
    abstract fun priceDao(): PriceDao

    companion object {
        @Volatile
        private var instance: FibBotDatabase? = null

        fun getInstance(context: Context): FibBotDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FibBotDatabase::class.java,
                    "fibbot_database"
                ).build().also { instance = it }
            }
        }
    }
}