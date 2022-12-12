package com.example.bookreport.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.BookMarkEntity
import com.example.bookreport.domain.BookMarkUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class BookMarkViewModel @Inject constructor(private val useCase: BookMarkUseCase) : ViewModel() {
    private val _bookMarkLiveData = MutableLiveData<BookMarkEntity>()
    val bookMarkLiveData: LiveData<BookMarkEntity>
        get() = _bookMarkLiveData
    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    private var saveJob: Job? = null

    suspend fun saveBookMark(bookMark: BookMark) = viewModelScope.launch {
        // delay
        useCase.saveBookMark(bookMark)
    }

    suspend fun deleteBookMark(bookMark: BookMark) {
            useCase.deleteBookMark(bookMark)
    }

    suspend fun loadBookMark() {
        viewModelScope.launch {
            // suspend 함수 호출
            _bookMarkLiveData.value = useCase.loadBookMark()
            Log.v("불러오기", "viewModel")

            //val result = useCase.loadBookMark()
            //val newList = bookMarkLiveData.value?.bookMarks.orEmpty() + result.bookMarks
            //_bookMarkLiveData.value = BookMarkEntity(useCase.loadBookMark().bookMarks)
        }
    }

    suspend fun loadBookMark2(): BookMarkEntity {
        return useCase.loadBookMark()
    }
}


