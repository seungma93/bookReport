package com.example.bookreport.data.remote

import com.example.bookreport.ResultSearchKeywordResponse
import com.example.bookreport.data.entity.KakaoBookResultEntity
import com.example.bookreport.data.mapper.toEntity
import com.example.bookreport.network.BookRetrofit
import com.example.bookreport.network.BookRetrofitImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoRemoteDataSource{
    suspend fun getSearchBook(keyword: String, page: Int) : ResultSearchKeywordResponse
}

class KakaoRemoteDataSourceImpl(private val api: BookRetrofit): KakaoRemoteDataSource {
   val kakaoApi = api.getRetrofit().create(KakaoAPI::class.java)

    override suspend fun getSearchBook(keyword: String, page: Int) : ResultSearchKeywordResponse {
        return kakaoApi.getSearchKeyword(
             keyword,
             page
         )
    }
}

interface KakaoAPI {
    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK e321aa2f13be335c0cf4ce8f9b1b3561"  // REST API 키
    }
    @GET("v3/search/book")    // Keyword.json의 정보를 받아옴
    suspend fun getSearchKeyword(
        //@Header("Authorization") key: String,     // 카카오 API 인증키 [필수]
        @Query("query") query: String,             // 검색을 원하는 질의어 [필수]
        // 매개변수 추가 가능
        // @Query("category_group_code") category: String
        @Query("page") page: Int
    ): ResultSearchKeywordResponse    // 받아온 정보가 ResultSearchKeyword 클래스의 구조로 담김
}
