package com.example.bookreport.presenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.bookreport.data.entity.KakaoBook
import com.example.bookreport.databinding.FragmentReportWriteBinding


class ReportWriteFragment: Fragment() {
    private lateinit var binding : FragmentReportWriteBinding
    private val kakaoBook get() = requireArguments().getSerializable(BookSearchFragment.KAKAO_BOOK_KEY) as KakaoBook
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportWriteBinding.inflate(inflater,container,false)
        binding.apply {
            bookTitle.text = kakaoBook.title
            bookContents.text = kakaoBook.contents
            Glide.with(requireContext()).load(kakaoBook.thumbnail).into(bookThumbnail)
        }
        return binding.root
    }
}