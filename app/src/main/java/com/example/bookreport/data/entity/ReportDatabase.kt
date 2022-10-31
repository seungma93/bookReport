package com.example.bookreport.data.entity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Report::class], version = 1)
abstract class ReportDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao

    companion object {
        private var instance: ReportDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ReportDatabase? {
            if (instance == null) {
                synchronized(ReportDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ReportDatabase::class.java,
                        "user.db"
                    ).build()
                }
            }
            return instance
        }
    }
}