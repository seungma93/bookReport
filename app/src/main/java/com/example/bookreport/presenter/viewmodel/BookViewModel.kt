package com.example.bookreport.presenter.viewmodel

import androidx.lifecycle.*
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.data.entity.KakaoBook
import com.example.bookreport.domain.KakaoBookUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


class BookViewModel @Inject constructor(private val useCase: KakaoBookUseCase) : ViewModel() {
    /*
    private val _bookLiveData = MutableLiveData<BookListEntity>()
    val bookLiveData: LiveData<BookListEntity>
        get() = _bookLiveData
     */
    private val bookListEntity: BookListEntity? = null
    private val _bookState = MutableStateFlow<BookListEntity?>(bookListEntity)
    val bookState: StateFlow<BookListEntity?> = _bookState.asStateFlow()
    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error
    private var isLoading = false

    fun insertKey(keyword: String, page: Int) {

        if (isLoading.not()) {
            // 코루틴 스코프 시작
            viewModelScope.launch {
                isLoading = true
                // suspend 함수 호출
                kotlin.runCatching {
                    val result = useCase.searchBook(keyword, page)
                    val old = bookState.value?.entities.orEmpty()
                    _bookState.value = BookListEntity(old + result.entities, result.meta)
                }.onFailure {
                    _error.value = it
                }
                delay(1000)
                isLoading = false
            }
        }
    }

    fun insertNewKey(keyword: String, page: Int) {
        if (!isLoading) {
            // 코루틴 스코프 시작
            viewModelScope.launch {
                isLoading = true
                // suspend 함수 호출
                kotlin.runCatching {
                    _bookState.value = useCase.searchBook(keyword, page)
                }.onFailure {
                    _error.value = it
                }
                isLoading = false
            }
        }
    }

    suspend fun refreshKey() = viewModelScope.launch {
        val old = bookState.value
        if (old != null) {
            _bookState.value = useCase.refreshBookMark(old)
        }
    }
}


