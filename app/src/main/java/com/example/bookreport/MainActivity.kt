package com.example.bookreport

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bookreport.databinding.ActivityMainBinding

sealed class EndPoint{
    data class Search(val sticky : Int): EndPoint()
    object Error: EndPoint()
}
interface BookReport{
    fun navigateFragment(endPoint: EndPoint)
}
class MainActivity: AppCompatActivity(), BookReport {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnFloating.setOnClickListener{
                val bookSearch = EndPoint.Search(0)
                navigateFragment(bookSearch)
            }
        }
    }

    private fun setFragmnet(fragment: Fragment) {
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
                    setFragmnet(fragment)
                }
                is EndPoint.Error -> {
                }
            }
        }
    }
}