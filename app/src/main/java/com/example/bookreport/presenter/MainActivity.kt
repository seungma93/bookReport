package com.example.bookreport.presenter


import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bookreport.R
import com.example.bookreport.data.entity.KakaoBook
import com.example.bookreport.databinding.ActivityMainBinding


sealed class EndPoint{
    data class Search(val sticky : Int): EndPoint()
    data class Write(val kakaoBook: KakaoBook): EndPoint()
    object Error: EndPoint()
}
interface BookReport{
    fun navigateFragment(endPoint: EndPoint)
}
class MainActivity: AppCompatActivity(), BookReport {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("onCreate","")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnFloating.setOnClickListener{
                val bookSearch = EndPoint.Search(0)
                navigateFragment(bookSearch)
                btnFloating.hide()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.v("onCreate","")
    }

    override fun onResume() {
        super.onResume()
        Log.v("onResume","")
    }

    override fun onStart() {
        super.onStart()
        Log.v("onStart","")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v("onRestart","")
    }

    override fun onPause() {
        super.onPause()
        Log.v("onPause","")
    }

    fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun navigateFragment(endPoint: EndPoint)  = with(Bundle()) {
        this.let {
            when (endPoint) {
                is EndPoint.Search -> {
                    val fragment = BookSearchFragment()
                    //it.putInt(PlayerListFragment.PLAYER_NUMBER_KEY, endPoint.playerNum)
                    //fragment.arguments = it
                    setFragment(fragment)
                }
                is EndPoint.Write -> {
                    val fragment = ReportWriteFragment()
                    it.putSerializable(BookSearchFragment.KAKAO_BOOK_KEY, endPoint.kakaoBook)
                    fragment.arguments = it
                    setFragment(fragment)
                }
                is EndPoint.Error -> {
                }
            }
        }
    }
}