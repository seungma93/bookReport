package com.example.bookreport

import androidx.compose.runtime.key
import androidx.lifecycle.*
import com.example.bookreport.data.entity.KakaoBookResultEntity
import com.example.bookreport.domain.KakaoBookUseCase
import kotlinx.coroutines.launch


class BookViewModel(private val useCase: KakaoBookUseCase) : ViewModel() {
    private val _liveData = MutableLiveData<KakaoBookResultEntity>()
    val liveData: LiveData<KakaoBookResultEntity>
    get() = _liveData
    private val _liveDataHaveKey = MutableLiveData<KakaoBookResultEntity>()
    val liveDataHaveKey: LiveData<KakaoBookResultEntity>
    get() = _liveDataHaveKey
    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error


    fun insertKey(keyword : String, page : Int) {
        // 코루틴 스코프 시작
        viewModelScope.launch {
            // suspend 함수 호출
            kotlin.runCatching {
                _liveData.value = useCase.searchBook(keyword, page)
            }.onFailure {
                _error.value = it
            }
        }
    }

    fun insertPage(keyword : String, page : Int) {
        // 코루틴 스코프 시작
        viewModelScope.launch {
            // suspend 함수 호출
            _liveDataHaveKey.value = useCase.searchBook(keyword, page)
        }
    }

}


