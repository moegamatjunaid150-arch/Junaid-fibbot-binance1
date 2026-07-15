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
    entities = [
        CandleEntity::class,
        TradeEntity::class,
        PriceCacheEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun candleDao(): CandleDao
    abstract fun tradeDao(): TradeDao
    abstract fun priceDao(): PriceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fibbot_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}