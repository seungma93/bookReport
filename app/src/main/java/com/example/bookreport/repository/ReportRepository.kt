package com.example.bookreport.repository

import android.util.Log
import com.example.bookreport.data.entity.Report
import com.example.bookreport.data.entity.ReportEntity
import com.example.bookreport.data.local.ReportLocalDataSource
import com.example.bookreport.data.mapper.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ReportRepository{
    suspend fun selectSave(report: Report)
    suspend fun loadData(): ReportEntity
}

class ReportRepositoryImpl(private val reportLocalDataSource: ReportLocalDataSource): ReportRepository {
    override suspend fun selectSave(report: Report) {
        withContext(Dispatchers.IO) {
            reportLocalDataSource.insert(report)
        }
    }
    override suspend fun loadData(): ReportEntity{
        val reportEntity : ReportEntity
        withContext(Dispatchers.IO) {
            reportEntity = reportLocalDataSource.select()
        }
        return reportEntity
    }

}