package com.example.bookreport.presenter


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bookreport.R
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.databinding.ActivityMainBinding
import com.example.bookreport.di.component.DaggerGoogleBooksComponent
import com.example.bookreport.di.component.DaggerKakaoBookComponent
import com.example.bookreport.di.component.GoogleBooksComponent
import com.example.bookreport.di.component.KakaoBookComponent
import com.example.bookreport.presenter.fragment.*


sealed class EndPoint {
    data class BookSearch(val bookType: String) : EndPoint()
    data class ReportWrite(val bookAndBookMark: BookAndBookMark) : EndPoint()
    data class ReportList(val sticky: Int) : EndPoint()
    data class BookMarkList(val sticky: Int): EndPoint()
    data class ReportEdit(val report: Report): EndPoint()
    object Error : EndPoint()
}

interface BookReport {
    fun navigateFragment(endPoint: EndPoint)
}

interface Dagger {
    val kakaoBookComponent: KakaoBookComponent
    val googleBooksComponent: GoogleBooksComponent
}

class MainActivity() : AppCompatActivity(), BookReport, Dagger {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override val googleBooksComponent: GoogleBooksComponent = DaggerGoogleBooksComponent.create()
    override val kakaoBookComponent: KakaoBookComponent = DaggerKakaoBookComponent.create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("생명주기", "onCreate")
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, ReportListFragment())
            .commit()

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
                is EndPoint.BookSearch -> {
                    val fragment = BookSearchFragment()
                    it.putSerializable(ReportListFragment.BOOK_TYPE_KEY, endPoint.bookType)
                    fragment.arguments = it
                    setFragment(fragment)
                }
                is EndPoint.ReportWrite -> {
                    val fragment = ReportWriteFragment()
                    it.putSerializable(BookSearchFragment.BOOK_AND_BOOKMARK, endPoint.bookAndBookMark)
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
                is EndPoint.ReportEdit -> {
                    val fragment = ReportEditFragment()
                    it.putSerializable(ReportEditFragment.REPORT_KEY, endPoint.report)
                    fragment.arguments = it
                    setFragment(fragment)
                }
                is EndPoint.Error -> {
                }
            }
        }
    }
}