package com.example.bookreport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class BookViewModel() : ViewModel() {
    private val _liveData = MutableLiveData<List<Book>>()
    val liveData: MutableLiveData<List<Book>>
    get() = _liveData

    fun insertKey(keyword : String) {
        // 코루틴 스코프 시작
        viewModelScope.launch {
            // suspend 함수 호출
            liveData.value = BookRetrofit.api.getSearchKeyword(
                KakaoAPI.API_KEY,
                keyword
            ).documents
        }
    }
}