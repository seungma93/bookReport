package com.example.bookreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.bookreport.databinding.FragmentBookSearchBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookSearchFragment: Fragment() {
    private val model: BookViewModel by activityViewModels()
    private lateinit var binding: FragmentBookSearchBinding
    private var bookData = mutableListOf<Book>()
    private val adapter = BookListAdapter { Book ->
        Toast.makeText(requireContext(), "참가자 ${Book.title} 입니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookSearchBinding.inflate(inflater,container,false)
        binding.bookList.adapter = adapter
        subscribe()
        binding.apply {
            btnSearch.setOnClickListener{
                val keyword = edit.text.toString()
                if( keyword != null){
                    model.insertKey(keyword)
                }
            }
        }
        return binding.root
    }
    // 뷰 모델 구독
    private fun subscribe() {
        // liveData 옵저버 viewLifecycleOwner : 라이프사이클 상태를 다양하게 가지고 있음
        model.liveData.observe(viewLifecycleOwner) {
            // 변경된 liveData 삽입
            bookData = it as MutableList<Book>
            adapter.setItems(it)
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
        .baseUrl(KakaoAPI.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(KakaoAPI::class.java)
}