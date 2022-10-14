package com.example.bookreport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class BookViewModel() : ViewModel() {
    private val _liveData = MutableLiveData<ResultSearchKeyword>()
    val liveData: MutableLiveData<ResultSearchKeyword>
    get() = _liveData
    private val api = BookRetrofit.getRetrofit().create(KakaoAPI::class.java)

    fun insertKey(keyword : String) {
        // 코루틴 스코프 시작
        viewModelScope.launch {
            // suspend 함수 호출
            liveData.value = api.getSearchKeyword(
                //KakaoAPI.API_KEY,
                keyword
            )
        }
    }

    fun insertPage(keyword : String, page : Int) {
        // 코루틴 스코프 시작
        viewModelScope.launch {
            // suspend 함수 호출
            liveData.value = api.getNextPage(
                //KakaoAPI.API_KEY,
                keyword,
                page
            )
        }
    }

}