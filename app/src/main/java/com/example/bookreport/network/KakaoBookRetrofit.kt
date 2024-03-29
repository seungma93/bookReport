package com.example.bookreport.network


import com.example.bookreport.data.remote.KakaoBookRemoteDataSource
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

// 레트로핏 object
object KakaoBookRetrofitImpl {
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
            .baseUrl(KakaoBookRemoteDataSource.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 인터셉터
    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", KakaoBookRemoteDataSource.API_KEY)
                .build()
            proceed(newRequest)
        }
    }
}
