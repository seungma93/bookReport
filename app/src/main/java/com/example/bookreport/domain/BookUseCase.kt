package com.example.bookreport.domain

import android.util.Log
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.BookEntity
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.repository.BookMarkRepository
import com.example.bookreport.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

interface BookUseCase {
    suspend fun searchBook(keyword: String, page: Int)
    fun subscribeSearchBook(): Flow<BookListEntity?>
}

class BookUseCaseImpl @Inject constructor(
    private val bookRepository: BookRepository,
    private val bookMarkRepository: BookMarkRepository
) : BookUseCase {

    override suspend fun searchBook(keyword: String, page: Int) {
        bookRepository.getBookEntity(keyword, page)
    }

    override fun subscribeSearchBook(): Flow<BookListEntity?> {
        return combine(
            bookMarkRepository.selectData(),
            bookRepository.subscribeBookEntity()
        ) { bookMark, book ->

            if (book != null) {
                val bookAndBookMark = book.items.map { bookItem ->
                    BookAndBookMark(
                        bookItem,
                        bookMark.find { it.title == bookItem.title })
                }
                when (book) {
                    is BookEntity.KakaoBookEntity -> {
                        BookListEntity.KakaoBookListEntity(bookAndBookMark, book.meta)
                    }
                    is BookEntity.GoogleBooksEntity -> {
                        BookListEntity.GoogleBooksListEntity(bookAndBookMark, book.meta)
                    }
                }
            } else {
                null
            }
        }
    }
}


        /*
        Log.v("BookUseCase", "시작")
        if (savedKeyword.isEmpty()) {

            val bookMarkFLow = bookMarkRepository.selectData()
            savedKeyword = keyword
            bookEntityStateFlow = bookRepository.getBookListEntity(keyword, page)

            bookListEntityFlow = bookMarkFLow.combine(
                bookEntityStateFlow
            ) { bookMark, bookEntity ->
                Log.v("BookUseCase", "combineFlow 시작")
                val oldList = savedBookListEntity?.entities?.map { entity ->
                    BookAndBookMark(
                        entity.bookDocuments,
                        bookMark.find { it.title == entity.bookDocuments.title }
                    )
                }
                Log.v("BookUseCase", "oldList " + oldList?.size.toString())
                val newList = bookEntity!!.items.map { entity ->
                    BookAndBookMark(
                        entity,
                        bookMark.find { it.title == entity.title }
                    )
                }
                val resultList = when (oldList) {
                    null -> newList
                    else -> {
                        when (oldList.find { it.bookDocuments.title == newList[0].bookDocuments.title }) {
                            null -> oldList + newList
                            else -> oldList
                        }
                    }
                }

                Log.v("BookUseCase", "resultList " + resultList.size.toString())

                val resultBookListEntity = when (bookEntity) {
                    is BookEntity.KakaoBookEntity -> {
                        BookListEntity.KakaoBookListEntity(resultList, bookEntity.meta)
                    }
                    is BookEntity.GoogleBooksEntity -> {
                        BookListEntity.GoogleBooksListEntity(resultList, bookEntity.meta)
                    }
                }

                savedBookListEntity = resultBookListEntity
                Log.v("BookUseCase", "combineFlow 끝 ")
                Log.v("BookUseCase", "리스트 갯수: " + savedBookListEntity?.entities?.size.toString())
                resultBookListEntity
            }

        } else if (savedKeyword == keyword) {
            savedBookListEntity = null
            searchBookEmit(keyword, page)
        } else {
            savedBookListEntity = null
            searchBookEmit(keyword, page)
        }

        Log.v("BookUseCase", "끝")
        return bookListEntityFlow
    }

         */


