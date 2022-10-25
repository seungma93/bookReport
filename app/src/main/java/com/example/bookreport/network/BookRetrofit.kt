package com.example.bookreport.network


import com.example.bookreport.data.remote.KakaoAPI
import com.example.bookreport.data.remote.KakaoAPI.Companion.API_KEY
import com.example.bookreport.data.remote.KakaoAPI.Companion.BASE_URL
import com.example.bookreport.data.remote.KakaoRemoteDataSourceImpl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

interface BookRetrofit{
    fun getRetrofit(): Retrofit
}

// 레트로핏 object
object BookRetrofitImpl: BookRetrofit {
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
    override fun getRetrofit(): Retrofit {
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
                .addHeader("Authorization", KakaoAPI.API_KEY)
                .build()
            proceed(newRequest)
        }
    }
}
