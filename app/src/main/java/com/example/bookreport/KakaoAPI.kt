package com.example.bookreport

import com.example.bookreport.KakaoAPI.Companion.API_KEY
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.io.IOException

interface KakaoAPI {
    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK e321aa2f13be335c0cf4ce8f9b1b3561"  // REST API 키
    }
    @GET("v3/search/book")    // Keyword.json의 정보를 받아옴
    // suspend 펑션
    suspend fun getSearchKeyword(
        //@Header("Authorization") key: String,     // 카카오 API 인증키 [필수]
        @Query("query") query: String             // 검색을 원하는 질의어 [필수]
        // 매개변수 추가 가능
        // @Query("category_group_code") category: String
    ): ResultSearchKeyword    // 받아온 정보가 ResultSearchKeyword 클래스의 구조로 담김

    @GET("v3/search/book")    // Keyword.json의 정보를 받아옴
    suspend fun getNextPage(
        //@Header("Authorization") key: String,     // 카카오 API 인증키 [필수]
        @Query("query") query: String,             // 검색을 원하는 질의어 [필수]
        // 매개변수 추가 가능
        // @Query("category_group_code") category: String
        @Query("page") page: Int
    ): ResultSearchKeyword    // 받아온 정보가 ResultSearchKeyword 클래스의 구조로 담김
}
// 레트로핏 object
object BookRetrofit {
    // 인터셉터 생성
    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    // 클라이언트 생성 인터셉터 삽입
    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(AppInterceptor())
        .build()
    // Retrofit 생성
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(KakaoAPI.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    // 인터셉터
    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", API_KEY)
                .build()
            proceed(newRequest)
        }
    }
}

