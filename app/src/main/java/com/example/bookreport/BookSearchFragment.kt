package com.example.bookreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bookreport.databinding.FragmentBookSearchBinding

class BookSearchFragment: Fragment() {
    private lateinit var binding: FragmentBookSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookSearchBinding.inflate(inflater,container,false)
        return binding.root
    }
}