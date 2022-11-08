package com.example.bookreport.domain

import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.entity.ReportEntity
import com.example.bookreport.repository.ReportRepository

interface ReportUseCase {
    suspend fun saveReport(report: Report)
    suspend fun loadReport(): ReportEntity
}

class ReportUseCaseImpl(private val reportRepository: ReportRepository) : ReportUseCase {

    override suspend fun saveReport(report: Report) {
        reportRepository.save(report)
    }

    override suspend fun loadReport(): ReportEntity {
        return reportRepository.loadData()
    }
}
