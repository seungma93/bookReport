package com.example.bookreport.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.BookEntity
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.data.entity.Meta
import com.example.bookreport.domain.BookUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class BookListViewModel (private val useCase: BookUseCase) : ViewModel() {
    /*
    private val _bookLiveData = MutableLiveData<BookListEntity>()
    val bookLiveData: LiveData<BookListEntity>
        get() = _bookLiveData
     */
    private val _bookState = MutableStateFlow<BookListEntity?>(null)
    val bookState: StateFlow<BookListEntity?> = _bookState.asStateFlow()
    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    suspend fun insertKey(keyword: String, page: Int) {
                kotlin.runCatching {
                    val result = useCase.searchBook(keyword, page)
                    val old = bookState.value?.entities.orEmpty()
                    _bookState.value = (old + result.entities).toBookMark(result)
                    //_bookState.value = BookListEntity.KakaoBookListEntity(old + result.entities, result.meta)
                }.onFailure {
                    _error.value = it
                }
    }

    suspend fun insertNewKey(keyword: String, page: Int) {
                kotlin.runCatching {
                    _bookState.value = useCase.searchBook(keyword, page)
                    Log.v("BookListViewModel", "동작")
                }.onFailure {
                    _error.value = it
                }
    }

    suspend fun refreshKey() = viewModelScope.launch {
        val old = bookState.value
        if (old != null) {
            _bookState.value = useCase.refreshBookMark(old)
        }
    }
}

// mapper

fun List<BookAndBookMark>.toBookMark(entity: BookListEntity): BookListEntity{
    return when(entity){
        is BookListEntity.KakaoBookListEntity -> BookListEntity.KakaoBookListEntity(this, entity.meta)
        is BookListEntity.GoogleBooksListEntity -> BookListEntity.GoogleBooksListEntity(this, entity.meta)
    }
}


