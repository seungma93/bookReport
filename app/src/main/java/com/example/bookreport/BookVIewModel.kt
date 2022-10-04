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

    fun insertKey(keyword : String) {
        viewModelScope.launch {
            liveData.value = BookRetrofit.api.getSearchKeyword(
                API_KEY,
                keyword
            ).documents
        }
    }
}