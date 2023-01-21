package com.example.bookreport.di.module

import android.content.Context
import com.example.bookreport.data.entity.room.BookMarkDatabase
import com.example.bookreport.data.entity.room.ReportDatabase
import dagger.Module
import dagger.Provides

class DatabaseModule {

    @Module
    class ReportDatabaseModule {
        @Provides
        fun providesReportDatabase(context: Context): ReportDatabase {
            return ReportDatabase.getInstance(context.applicationContext)!!
            //Room.databaseBuilder<ReportDatabase>(applcation, ReportDatabase::class.java,"user.db").build()
        }
    }

    @Module
    class BookMarkDatabaseModule {
        @Provides
        fun providesBookMarkDatabase(context: Context): BookMarkDatabase {
            return BookMarkDatabase.getInstance(context.applicationContext)!!
        }
    }
}