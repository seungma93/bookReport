package com.example.bookreport.domain

import androidx.room.PrimaryKey
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.entity.ReportEntity
import com.example.bookreport.repository.ReportRepository
import javax.inject.Inject

interface ReportUseCase {
    suspend fun saveReport(report: Report)
    suspend fun loadReport(): ReportEntity
    suspend fun editReport(report: Report)
}

class ReportUseCaseImpl @Inject constructor(private val reportRepository: ReportRepository) : ReportUseCase {

    override suspend fun saveReport(report: Report) {
        reportRepository.save(report)
    }

    override suspend fun loadReport(): ReportEntity {
        return reportRepository.loadData()
    }

    override suspend fun editReport(report: Report) {
        reportRepository.update(report)
    }




}
