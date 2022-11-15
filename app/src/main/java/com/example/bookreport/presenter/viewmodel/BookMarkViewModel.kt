package com.example.bookreport.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.BookMarkEntity
import com.example.bookreport.domain.BookMarkUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class BookMarkViewModel(private val useCase: BookMarkUseCase) : ViewModel() {
    private val _bookMarkLiveData = MutableLiveData<BookMarkEntity>()
    val bookMarkLiveData: LiveData<BookMarkEntity>
        get() = _bookMarkLiveData
    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    private var saveJob: Job? = null

    fun saveBookMark(bookMark: BookMark) {
        // 코루틴 스코프 시작
        if (saveJob?.isActive == true) return
        saveJob = viewModelScope.launch {
            // suspend 함수 호출
            useCase.saveBookMark(bookMark)
        }
    }

    fun deleteBookMark(bookMark: BookMark) {
        // 코루틴 스코프 시작
        viewModelScope.launch {
            // suspend 함수 호출
            useCase.deleteBookMark(bookMark)
        }
    }

    fun loadBookMark() {
        viewModelScope.launch {
            // suspend 함수 호출
            //_bookMarkLiveData.value = useCase.loadBookMark()
            Log.v("불러오기", "viewModel")

            //val result = useCase.loadBookMark()
            //val newList = bookMarkLiveData.value?.bookMarks.orEmpty() + result.bookMarks
            _bookMarkLiveData.value = BookMarkEntity(useCase.loadBookMark().bookMarks)
        }
    }
}


