package com.example.bookreport.repository

import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.entity.ReportEntity
import com.example.bookreport.data.local.ReportLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ReportRepository{
    suspend fun save(report: Report)
    suspend fun loadData(): ReportEntity
}

class ReportRepositoryImpl(private val reportLocalDataSource: ReportLocalDataSource): ReportRepository {
    override suspend fun save(report: Report) {
        withContext(Dispatchers.IO) {
            reportLocalDataSource.insert(report)
        }
    }
    override suspend fun loadData(): ReportEntity{
        return withContext(Dispatchers.IO) {
            reportLocalDataSource.select()
        }
    }

}