package com.example.bookreport.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreport.data.entity.BookMarkEntity
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.domain.BookMarkUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class BookMarkViewModel (private val useCase: BookMarkUseCase) : ViewModel() {
    /*
    private val _bookMarkLiveData = MutableLiveData<BookMarkEntity>()
    val bookMarkLiveData: LiveData<BookMarkEntity>
        get() = _bookMarkLiveData
     */
    private val bookMarkEntity: BookMarkEntity? = null
    private val _bookMarkState = MutableStateFlow<BookMarkEntity?>(bookMarkEntity)
    val bookMarkState: StateFlow<BookMarkEntity?> = _bookMarkState.asStateFlow()

    suspend fun saveBookMark(bookMark: BookMark) = viewModelScope.launch {
            useCase.saveBookMark(bookMark)
    }
    suspend fun deleteBookMark(bookMark: BookMark) {
            useCase.deleteBookMark(bookMark)
    }
    suspend fun loadBookMark() = viewModelScope.launch {
            useCase.loadBookMark().collect() {
                _bookMarkState.value = BookMarkEntity(it)
            }
    }
}


