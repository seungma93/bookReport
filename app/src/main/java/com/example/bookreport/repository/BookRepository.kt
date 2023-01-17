package com.example.bookreport.repository

import android.util.Log
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.BookEntity
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.data.entity.BookMarkEntity
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.mapper.toEntity
import com.example.bookreport.data.remote.GoogleBooksRemoteDataSource
import com.example.bookreport.data.remote.KakaoBookRemoteDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map


interface BookRepository {
    suspend fun getBookListEntity(keyword: String, page: Int): Flow<BookListEntity>
    suspend fun refreshBookMark(entity: BookListEntity): BookListEntity
    fun test(): List<BookMark>
}

class BookRepositoryImpl(
    private val dataSourceToRepository: DataSourceToRepository,
    private val bookMarkRepository: BookMarkRepository
) : BookRepository {
    override suspend fun getBookListEntity(
        keyword: String,
        page: Int
    ): Flow<BookListEntity> {
            val bookEntityFlow = dataSourceToRepository.toRepository(keyword, page)
            val bookMarkFlow = bookMarkRepository.selectData()
            val bookListEntityFlow = bookEntityFlow.combine(bookMarkFlow) { bookEntity, bookMark ->

                    Log.v("BookRepo", "isbn" + bookEntity.items.map { it.isbn })

                    bookEntity.items.map { entity ->
                        BookAndBookMark(entity, bookMark.find { it.title == entity.title })
                    }.let {
                        when (bookEntity){
                            is BookEntity.KakaoBookEntity -> {
                                BookListEntity.KakaoBookListEntity(it, bookEntity.meta)
                            }
                            is BookEntity.GoogleBooksEntity -> {
                                BookListEntity.GoogleBooksListEntity(it, bookEntity.meta)
                            }
                        }
                    }
                }
        return bookListEntityFlow
    }

    override fun test(): List<BookMark> {
        val scope = CoroutineScope(Dispatchers.IO)
        var bookMark = listOf<BookMark>()
        val job = scope.launch {
            bookMarkRepository.selectData().map{
                it
            }.collect {
                bookMark = it
            }
        }
        return bookMark
    }



    override suspend fun refreshBookMark(entity: BookListEntity): BookListEntity {

        var bookMark = listOf<BookMark>()
            bookMarkRepository.selectData().collect {
                bookMark = it
            }
        return when (entity) {
            is BookListEntity.KakaoBookListEntity -> {
                entity.entities.map { bookAndBookMark ->
                    BookAndBookMark(
                        bookAndBookMark.bookDocuments,
                        bookMark.find { it.title == bookAndBookMark.bookDocuments.title })
                }.let {
                    BookListEntity.KakaoBookListEntity(it, entity.meta)
                }
            }
            is BookListEntity.GoogleBooksListEntity -> {
                entity.entities.map { bookAndBookMark ->
                    BookAndBookMark(
                        bookAndBookMark.bookDocuments,
                        bookMark.find { it.title == bookAndBookMark.bookDocuments.title })
                }.let {
                    BookListEntity.GoogleBooksListEntity(it, entity.meta)
                }
            }
        }
    }
}