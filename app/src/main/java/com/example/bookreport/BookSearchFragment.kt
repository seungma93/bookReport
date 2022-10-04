package com.example.bookreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.bookreport.BookSearchFragment.Companion.BASE_URL
import com.example.bookreport.databinding.FragmentBookSearchBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookSearchFragment: Fragment() {
    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
    }
    private val model: BookViewModel by activityViewModels()
    private lateinit var binding: FragmentBookSearchBinding
    private var test = mutableListOf<Book>()
    private val adapter = BookListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookSearchBinding.inflate(inflater,container,false)
        subscribe()
        binding.apply {

            btnSearch.setOnClickListener{
                model.insertKey("서시")
            }
        }
        return binding.root
    }

    private fun subscribe() {
        model.liveData.observe(requireActivity()) {
            test = it as MutableList<Book>
            adapter.datalist = test
            binding.bookList.adapter = adapter

        }
    }

}



object BookRetrofit{
    val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()   // Retrofit 구성
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(KakaoAPI::class.java)
}