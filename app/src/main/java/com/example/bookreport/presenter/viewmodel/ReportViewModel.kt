package com.example.bookreport.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.PrimaryKey
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.entity.ReportEntity
import com.example.bookreport.domain.ReportUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReportViewModel @Inject constructor(private val useCase: ReportUseCase) : ViewModel() {
    private val _liveData = MutableLiveData<ReportEntity>()
    val liveData: LiveData<ReportEntity>
        get() = _liveData
    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    suspend fun save(report: Report) {
        // 코루틴 스코프 시작
        viewModelScope.launch {
            // suspend 함수 호출
            kotlin.runCatching {
                useCase.saveReport(report)
            }.onFailure {
                _error.value = it
            }
        }
    }

    suspend fun load() {
        // 코루틴 스코프 시작
        viewModelScope.launch {
            // suspend 함수 호출
            kotlin.runCatching {
                _liveData.value = useCase.loadReport()
            }.onFailure {
                _error.value = it
            }
        }
    }

    suspend fun edit(report: Report){
        viewModelScope.launch {
            // suspend 함수 호출
            kotlin.runCatching {
                useCase.editReport(report)
            }.onFailure {
                _error.value = it
            }
        }
    }
}
