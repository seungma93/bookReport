package com.example.bookreport.presenter


import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bookreport.R
import com.example.bookreport.data.entity.KakaoBook
import com.example.bookreport.databinding.ActivityMainBinding


sealed class EndPoint {
    data class Search(val sticky: Int) : EndPoint()
    data class Write(val kakaoBook: KakaoBook) : EndPoint()
    data class List(val sticky: Int) : EndPoint()
    object Error : EndPoint()
}

interface BookReport {
    fun navigateFragment(endPoint: EndPoint)
}

class MainActivity : AppCompatActivity(), BookReport {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("onCreate", "")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val reportList = EndPoint.List(0)
        navigateFragment(reportList)
    }


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.v("onCreate", "")
    }

    override fun onResume() {
        super.onResume()
        Log.v("onResume", "")
    }

    override fun onStart() {
        super.onStart()
        Log.v("onStart", "")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v("onRestart", "")
    }

    override fun onPause() {
        super.onPause()
        Log.v("onPause", "")
    }

    fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun navigateFragment(endPoint: EndPoint) = with(Bundle()) {
        this.let {
            when (endPoint) {
                is EndPoint.Search -> {
                    val fragment = BookSearchFragment()
                    setFragment(fragment)
                }
                is EndPoint.Write -> {
                    val fragment = ReportWriteFragment()
                    it.putSerializable(BookSearchFragment.KAKAO_BOOK_KEY, endPoint.kakaoBook)
                    fragment.arguments = it
                    setFragment(fragment)
                }
                is EndPoint.List -> {
                    val fragment = ReportListFragment()
                    setFragment(fragment)
                }
                is EndPoint.Error -> {
                }
            }
        }
    }
}