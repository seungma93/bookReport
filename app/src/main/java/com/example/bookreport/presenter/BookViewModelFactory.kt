package com.example.bookreport.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.BookViewModel
import com.example.bookreport.domain.KakaoBookUseCase
import com.example.bookreport.domain.KakaoBookUseCaseImpl

class BookViewModelFactory(private val useCase: KakaoBookUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        // modelClass에 MainActivityViewModel이 상속이 되었나요? 라고 물어봅니다.
        if(modelClass.isAssignableFrom(BookViewModel::class.java)){
            // 맞다면 MainViewModel의 파라미터 값을 넘겨주죠.
            return BookViewModel(useCase) as T
        }
        // 상속이 되지 않았다면 IllegalArgumentException을 통해 상속이 되지 않았다는 에러를 띄웁니다.
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

