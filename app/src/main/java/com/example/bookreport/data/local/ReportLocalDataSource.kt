package com.example.bookreport.data.local

import android.content.Context
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.entity.room.ReportDatabase
import com.example.bookreport.data.entity.ReportEntity

interface ReportLocalDataSource {
    suspend fun insert(report: Report)
    suspend fun select(): ReportEntity
}

class ReportLocalDataSourceImpl(private val context: Context) : ReportLocalDataSource {
    private val db = ReportDatabase.getInstance(context.applicationContext)!!

    override suspend fun insert(report: Report) {
        db.reportDao().insert(report)
    }

    override suspend fun select(): ReportEntity {
        return ReportEntity(db.reportDao().getAll())
    }
}