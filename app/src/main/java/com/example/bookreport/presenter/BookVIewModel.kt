package com.example.bookreport

import androidx.compose.runtime.key
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.*
import com.example.bookreport.data.entity.KakaoBookResultEntity
import com.example.bookreport.domain.KakaoBookUseCase
import kotlinx.coroutines.launch


class BookViewModel(private val useCase: KakaoBookUseCase) : ViewModel() {
    private val _liveData = MutableLiveData<KakaoBookResultEntity>()
    val liveData: LiveData<KakaoBookResultEntity>
        get() = _liveData
    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error
    private var isLoading = false

    fun insertKey(keyword: String, page: Int) {

        if (isLoading == false) {
            // 코루틴 스코프 시작
            viewModelScope.launch {
                isLoading = true
                // suspend 함수 호출
                kotlin.runCatching {
                    val result = useCase.searchBook(keyword, page)
                    val newList = liveData.value?.documents.orEmpty() + result.documents
                    _liveData.value = KakaoBookResultEntity(
                        meta = result.meta,
                        documents = newList
                    )
                }.onFailure {
                    _error.value = it
                }
                isLoading = false
            }
        }
    }

    fun insertNewKey(keyword: String, page: Int){
        if (isLoading == false) {
            // 코루틴 스코프 시작
            viewModelScope.launch {
                isLoading = true
                // suspend 함수 호출
                kotlin.runCatching {
                    _liveData.value = useCase.searchBook(keyword, page)
                }.onFailure {
                    _error.value = it
                }
                isLoading = false
            }
        }
    }
}


