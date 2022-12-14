package com.example.bookreport.repository

import androidx.room.PrimaryKey
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.entity.ReportEntity
import com.example.bookreport.data.local.ReportLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ReportRepository{
    suspend fun save(report: Report)
    suspend fun loadData(): ReportEntity
    suspend fun update(report: Report)
}

class ReportRepositoryImpl @Inject constructor(private val reportLocalDataSource: ReportLocalDataSource): ReportRepository {
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

    override suspend fun update(report: Report) {
        withContext(Dispatchers.IO){
            reportLocalDataSource.update(report)
        }
    }


}