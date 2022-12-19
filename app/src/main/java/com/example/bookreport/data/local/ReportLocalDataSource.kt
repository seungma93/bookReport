package com.example.bookreport.data.local

import android.content.Context
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.entity.room.ReportDatabase
import com.example.bookreport.data.entity.ReportEntity
import javax.inject.Inject

interface ReportLocalDataSource {
    suspend fun insert(report: Report)
    suspend fun select(): ReportEntity
    suspend fun update(report: Report)
}

class ReportLocalDataSourceImpl @Inject constructor(private val db: ReportDatabase) :
    ReportLocalDataSource {
    override suspend fun insert(report: Report) {
        db.reportDao().insert(report)
    }

    override suspend fun select(): ReportEntity {
        return ReportEntity(db.reportDao().getAll())
    }

    override suspend fun update(report: Report) {
        db.reportDao().update(report)
    }
}