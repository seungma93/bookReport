package com.example.bookreport.presenter


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bookreport.R
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.databinding.ActivityMainBinding
import com.example.bookreport.presenter.fragment.BookMarkListFragment
import com.example.bookreport.presenter.fragment.BookSearchFragment
import com.example.bookreport.presenter.fragment.ReportListFragment
import com.example.bookreport.presenter.fragment.ReportWriteFragment


sealed class EndPoint {
    data class Search(val sticky: Int) : EndPoint()
    data class ReportWrite(val bookAndBookMark: BookAndBookMark, val isSelected: Boolean) : EndPoint()
    data class ReportList(val sticky: Int) : EndPoint()
    data class BookMarkList(val sticky: Int): EndPoint()
    object Error : EndPoint()
}

interface BookReport {
    fun navigateFragment(endPoint: EndPoint)
}

class MainActivity : AppCompatActivity(), BookReport {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("생명주기", "onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val reportList = EndPoint.ReportList(0)
        navigateFragment(reportList)
    }


    override fun onResume() {
        super.onResume()
        Log.v("생명주기", "onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.v("생명주기", "onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v("생명주기", "onRestart")
    }

    override fun onPause() {
        super.onPause()
        Log.v("생명주기", "onPause")
    }

    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun navigateFragment(endPoint: EndPoint) {

        Bundle().let {
            when (endPoint) {
                is EndPoint.Search -> {
                    val fragment = BookSearchFragment()
                    setFragment(fragment)
                }
                is EndPoint.ReportWrite -> {
                    val fragment = ReportWriteFragment()
                    it.putSerializable(BookSearchFragment.KAKAO_BOOK_KEY, endPoint.bookAndBookMark)
                    it.putBoolean(BookSearchFragment.BOOK_MARK_KEY, endPoint.isSelected)
                    fragment.arguments = it
                    setFragment(fragment)
                }
                is EndPoint.ReportList -> {
                    val fragment = ReportListFragment()
                    setFragment(fragment)
                }
                is EndPoint.BookMarkList -> {
                    val fragment = BookMarkListFragment()
                    setFragment(fragment)
                }
                is EndPoint.Error -> {
                }
            }
        }
    }
}