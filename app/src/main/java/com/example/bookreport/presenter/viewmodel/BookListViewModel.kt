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
import com.example.bookreport.data.mapper.toBookMark
import com.example.bookreport.domain.BookUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class BookListViewModel(private val useCase: BookUseCase) : ViewModel() {
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
            useCase.searchBook(keyword, page).collect {
                _bookState.value = it
            }
            //_bookState.value = BookListEntity.KakaoBookListEntity(old + result.entities, result.meta)
        }.onFailure {
            _error.value = it
        }
    }

    fun insertNewKey(keyword: String, page: Int) {
        Log.v("BookListViewModel", "시작")
        kotlin.runCatching {
            viewModelScope.launch {
                useCase.searchBook(keyword, page).collect {
                    _bookState.value = it
                    it.entities.map { Log.v("BookListViewModel", it.bookDocuments.title) }
                    it.entities.map { Log.v("북마크", it.bookMark?.title.orEmpty()) }
                }
            }
        }.onFailure {
            it.printStackTrace()
        }

    }

    suspend fun refreshKey() = viewModelScope.launch {
        val old = bookState.value
        if (old != null) {
            _bookState.value = useCase.refreshBookMark(old)
        }
    }

}




