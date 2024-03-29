package com.example.bookreport.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookreport.data.entity.ReportEntity
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.domain.ReportUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ReportViewModel @Inject constructor(private val useCase: ReportUseCase) : ViewModel() {
    /*
    private val _liveData = MutableLiveData<ReportEntity>()
    val liveData: LiveData<ReportEntity>
        get() = _liveData
     */
    private val reportEntity: ReportEntity? = null
    private val _reportState = MutableStateFlow<ReportEntity?>(reportEntity)
    val reportState: StateFlow<ReportEntity?> = _reportState.asStateFlow()
    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    suspend fun save(report: Report) {
        kotlin.runCatching {
            useCase.saveReport(report)
        }.onFailure {
            Log.v("ReportViewModel", "실패")
            //_error.value = it
            it.printStackTrace()
        }
    }

    suspend fun load() {
        kotlin.runCatching {
            _reportState.value = useCase.loadReport()
        }.onFailure {
            _error.value = it
        }
    }

    suspend fun edit(report: Report) {
        kotlin.runCatching {
            useCase.editReport(report)
        }.onFailure {
            _error.value = it
        }
    }
}
