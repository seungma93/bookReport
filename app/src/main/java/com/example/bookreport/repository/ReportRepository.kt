package com.example.bookreport.repository

import com.example.bookreport.data.entity.ReportEntity
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.local.ReportLocalDataSource
import javax.inject.Inject

interface ReportRepository{
    suspend fun save(report: Report)
    suspend fun loadData(): ReportEntity
    suspend fun update(report: Report)
}

class ReportRepositoryImpl (
    private val reportLocalDataSource: ReportLocalDataSource): ReportRepository {

    override suspend fun save(report: Report) {
            reportLocalDataSource.insert(report)
    }
    override suspend fun loadData(): ReportEntity{
        return reportLocalDataSource.select()
    }

    override suspend fun update(report: Report) {
            reportLocalDataSource.update(report)
    }

}