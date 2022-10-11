package com.example.bookreport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class BookViewModel() : ViewModel() {
    companion object {
        const val API_KEY = "KakaoAK e321aa2f13be335c0cf4ce8f9b1b3561"  // REST API 키
    }
    private val _liveData = MutableLiveData<List<Book>>()
    val liveData: MutableLiveData<List<Book>>
    get() = _liveData
    private val _liveDataTest = MutableLiveData<List<Book>>()
    val liveDataTest: MutableLiveData<List<Book>>
    get() = _liveDataTest

    fun insertKey(keyword : String) {
        // 코루틴 스코프 시작
        viewModelScope.launch {
            // suspend 함수 호출
            liveData.value = BookRetrofit.api.getSearchKeyword(
                API_KEY,
                keyword
            ).documents
        }
    }

    fun testKey(keyword: String) {
        viewModelScope.launch {
            liveDataTest.value = BookRetrofit.api.getSearchKeyword(
                API_KEY,
                keyword
            ).documents
        }
    }
}