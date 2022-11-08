package com.example.bookreport

import androidx.lifecycle.*
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.data.entity.KakaoBook
import com.example.bookreport.data.entity.KakaoBookResultEntity
import com.example.bookreport.domain.KakaoBookUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class BookViewModel(private val useCase: KakaoBookUseCase) : ViewModel() {
    private val _bookLiveData = MutableLiveData<BookListEntity>()
    val bookLiveData: LiveData<BookListEntity>
        get() = _bookLiveData
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
                    val old = bookLiveData.value?.entities.orEmpty()
                    _bookLiveData.value = BookListEntity(old + result.entities, result.meta)
                }.onFailure {
                    _error.value = it
                }
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
                    _bookLiveData.value = useCase.searchBook(keyword, page)
                }.onFailure {
                    _error.value = it
                }
                isLoading = false
            }
        }
    }
}


