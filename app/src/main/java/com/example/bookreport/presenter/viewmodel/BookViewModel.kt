package com.example.bookreport.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.domain.KakaoBookUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class BookViewModel @Inject constructor(private val useCase: KakaoBookUseCase) : ViewModel() {
    /*
    private val _bookLiveData = MutableLiveData<BookListEntity>()
    val bookLiveData: LiveData<BookListEntity>
        get() = _bookLiveData
     */
    private val _bookState = MutableStateFlow<BookListEntity.KakaoBookBookListEntity?>(null)
    val bookState: StateFlow<BookListEntity.KakaoBookBookListEntity?> = _bookState.asStateFlow()
    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    suspend fun insertKey(keyword: String, page: Int) {
                kotlin.runCatching {
                    val result = useCase.searchBook(keyword, page)
                    val old = bookState.value?.entities.orEmpty()
                    _bookState.value = BookListEntity.KakaoBookBookListEntity(old + result.entities, result.meta)
                }.onFailure {
                    _error.value = it
                }
    }

    suspend fun insertNewKey(keyword: String, page: Int) {
                kotlin.runCatching {
                    _bookState.value = useCase.searchBook(keyword, page)
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


