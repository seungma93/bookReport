package com.example.bookreport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class BookViewModel() : ViewModel() {
    private val _liveData = MutableLiveData<ResultSearchKeyword>()
    val liveData: LiveData<ResultSearchKeyword>
    get() = _liveData
    private val _liveDataHaveKey = MutableLiveData<ResultSearchKeyword>()
    val liveDataHaveKey: LiveData<ResultSearchKeyword>
    get() = _liveDataHaveKey
    private val api = BookRetrofit.getRetrofit().create(KakaoAPI::class.java)

    fun insertKey(keyword : String, page : Int) {
        // 코루틴 스코프 시작
        viewModelScope.launch {
            // suspend 함수 호출
            _liveData.value = api.getSearchKeyword(
                //KakaoAPI.API_KEY,
                keyword,
                page
            )
        }
    }

    fun insertPage(keyword : String, page : Int) {
        // 코루틴 스코프 시작
        viewModelScope.launch {
            // suspend 함수 호출
            _liveDataHaveKey.value = api.getSearchKeyword(
                //KakaoAPI.API_KEY,
                keyword,
                page
            )
        }
    }

}