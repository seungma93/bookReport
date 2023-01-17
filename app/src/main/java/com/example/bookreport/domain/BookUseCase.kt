package com.example.bookreport.domain

import android.util.Log
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.data.entity.BookMarkEntity
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.mapper.toBookMark
import com.example.bookreport.repository.BookMarkRepository
import com.example.bookreport.repository.BookRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

interface BookUseCase {
    suspend fun searchBook(keyword: String, page: Int): Flow<BookListEntity>
    suspend fun refreshBookMark(entity: BookListEntity): BookListEntity
}

class BookUseCaseImpl(
    private val bookRepository: BookRepository,
    private val bookMarkRepository: BookMarkRepository
) : BookUseCase {

    private val _bookState = MutableStateFlow<BookListEntity?>(null)
    val bookState: StateFlow<BookListEntity?> = _bookState.asStateFlow()

    override suspend fun searchBook(keyword: String, page: Int): Flow<BookListEntity> {
        Log.v("BookUseCase", "시작")
        //return bookRepository.getBookListEntity(keyword, page)

        val bookListEntityFlow = bookRepository.getBookListEntity(keyword, page)

        var oldList = when(page){
            1 -> emptyList()
            else -> bookState?.value?.entities.orEmpty()
        }

        val resultFlow = bookListEntityFlow.map {
            val result = oldList + it.entities
            val resultEntity = result.toBookMark(it)
            _bookState.value = resultEntity
            resultEntity
        }

        return resultFlow

        Log.v("BookUseCase", "끝")


    }

    override suspend fun refreshBookMark(entity: BookListEntity): BookListEntity {
        return bookRepository.refreshBookMark(entity)
    }
}

