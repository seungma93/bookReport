package com.example.bookreport.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.domain.BookUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class BookListViewModel (private val useCase: BookUseCase) : ViewModel() {
    /*
    private val _bookLiveData = MutableLiveData<BookListEntity>()
    val bookLiveData: LiveData<BookListEntity>
        get() = _bookLiveData
     */
    private val _bookState = MutableStateFlow<BookListEntity?>(null)
    val bookState: StateFlow<BookListEntity?> = combine(
        _bookState,
        useCase.subscribeSearchBook()
    ){bookState, subscribeBookState ->
        subscribeBookState
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error



    fun insertKey(keyword: String, page: Int) {

            viewModelScope.launch {
                kotlin.runCatching {
                    useCase.searchBook(keyword, page)
                }.onFailure {
                    it.printStackTrace()
                }
            }
    }

    fun insertNewKey(keyword: String, page: Int) {

        viewModelScope.launch {
            kotlin.runCatching {
                useCase.searchNewBook(keyword, page)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }


    /*
    fun insertNewKey(keyword: String, page: Int) {
        Log.v("BookListViewModel", "시작")
        kotlin.runCatching {
            viewModelScope.launch {
                useCase.searchBookCollect(keyword, page).collectLatest {
                    Log.v("collect", "시작")
                    _bookState.value = it
                    it.entities.map { Log.v("BookListViewModel", it.bookDocuments.title) }
                    it.entities.map { Log.v("북마크", it.bookMark?.title.orEmpty()) }
                    Log.v("BookListViewModel", "엔티티 사이즈 " + it.entities.size)
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

     */
}




