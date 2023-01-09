package com.example.bookreport.repository

import android.util.Log
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.BookEntity
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.data.mapper.toEntity
import com.example.bookreport.data.remote.GoogleBooksRemoteDataSource
import com.example.bookreport.data.remote.KakaoBookRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.processNextEventInCurrentThread
import kotlinx.coroutines.withContext


interface BookRepository {
    suspend fun getBookListEntity(keyword: String, startIndex: Int): BookListEntity
    suspend fun refreshBookMark(entity: BookListEntity): BookListEntity
}

class BookRepositoryImpl(
    private val dataSourceToRepository: DataSourceToRepository,
    private val bookMarkRepository: BookMarkRepository
) : BookRepository {
    override suspend fun getBookListEntity(
        keyword: String,
        startIndex: Int
    ): BookListEntity {
        return withContext(Dispatchers.IO) {
            val bookEntityAsync = async {
                dataSourceToRepository.toRepository(keyword, startIndex) }
            val bookMarkAsync = async {
                bookMarkRepository.selectData().bookMarks }
            val bookEntity = bookEntityAsync.await()
            val bookMark = bookMarkAsync.await()

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
    }

    override suspend fun refreshBookMark(entity: BookListEntity): BookListEntity {
        val bookMark = bookMarkRepository.selectData().bookMarks
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