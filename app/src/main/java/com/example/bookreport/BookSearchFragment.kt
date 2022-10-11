package com.example.bookreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import java.util.SimpleTimeZone

class BookSearchFragment: Fragment() {
    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
    }
    private val model: BookViewModel by activityViewModels()
    private lateinit var binding: FragmentBookSearchBinding
    private var bookData = mutableListOf<Book>()
    //private val adapter = BookListAdapter()
    private val adapter = BookListAdapter { Book ->
        Toast.makeText(requireContext(), "참가자 ${Book.title} 입니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookSearchBinding.inflate(inflater,container,false)
        subscribe()
        binding.apply {
            btnSearch.setOnClickListener{
                val keyword = edit.text.toString()
                if( keyword != null){
                    model.insertKey(keyword)
                }
            }
            btnTest.setOnClickListener {
                model.testKey("서시")
                model.liveDataTest.observe(requireActivity()) {
                    adapter.setItems(it)
                    binding.bookList.adapter = adapter
                }
            }
        }
        return binding.root
    }
    // 뷰 모델 구독
    private fun subscribe() {
        // liveData 옵저버
        model.liveData.observe(requireActivity()) {
            // 변경된 liveData 삽입
            bookData = it as MutableList<Book>
            adapter.datalist = bookData
            binding.bookList.adapter = adapter
        }
    }
}

object BookRetrofit{
    // 인터셉터 생성
    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    // 클라이언트 생성 인터셉터 삽입
    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    // Retrofit 생성
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(KakaoAPI::class.java)
}