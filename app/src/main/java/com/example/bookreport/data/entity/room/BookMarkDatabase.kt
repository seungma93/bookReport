package com.example.bookreport.data.entity.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookMark::class], version = 1)
abstract class BookMarkDatabase : RoomDatabase() {
    abstract fun BookMarkDao(): BookMarkDao

    companion object {
        private var instance: BookMarkDatabase? = null

        @Synchronized
        fun getInstance(context: Context): BookMarkDatabase? {
            if (instance == null) {
                synchronized(ReportDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookMarkDatabase::class.java,
                        "BookMark.db"
                    ).build()
                }
            }
            return instance
        }
    }
}