package com.example.bookreport.domain

import android.util.Log
import com.example.bookreport.data.entity.Report
import com.example.bookreport.data.entity.ReportEntity
import com.example.bookreport.repository.ReportRepository

interface ReportUseCase {
    suspend fun saveReport(report: Report)
    suspend fun loadReport(): ReportEntity
}

class SaveReportUseCaseImpl(private val reportRepository: ReportRepository) : ReportUseCase {

    override suspend fun saveReport(report: Report) {
        reportRepository.selectSave(report)
    }

    override suspend fun loadReport(): ReportEntity {
        return reportRepository.loadData()
    }
}
