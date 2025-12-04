package com.example.a7minutesworkoutapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a7minutesworkoutapp.data.dao.HistoryDao
import com.example.a7minutesworkoutapp.data.entity.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 2)
abstract class HistoryDatabase: RoomDatabase() {

    abstract fun historyDao(): HistoryDao
    companion object{
        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        fun getInstance(context: Context): HistoryDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                                        context.applicationContext,
                                        HistoryDatabase::class.java,
                                        "history-database"
                    ).fallbackToDestructiveMigration(false).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}