package com.example.bookreport.data.remote

import com.example.bookreport.data.entity.response.GoogleBooksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksRemoteDataSource {
    companion object {
        const val BASE_URL = "https://www.googleapis.com/"
        //const val API_KEY = "KakaoAK e321aa2f13be335c0cf4ce8f9b1b3561"  // REST API 키
    }

    @GET("books/v1/volumes")    // Keyword.json의 정보를 받아옴
    suspend fun getSearchKeyword(
        //@Header("Authorization") key: String,     // 카카오 API 인증키 [필수]
        @Query("q") query: String,             // 검색을 원하는 질의어 [필수]
        // 매개변수 추가 가능
        // @Query("category_group_code") category: String
        @Query("startIndex") startIndex: Int
    ): GoogleBooksResponse    // 받아온 정보가 ResultSearchKeyword 클래스의 구조로 담김
}
