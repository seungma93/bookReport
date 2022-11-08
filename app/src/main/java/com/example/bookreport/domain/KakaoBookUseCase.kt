package com.example.bookreport.domain

import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.repository.BookMarkRepository
import com.example.bookreport.repository.KakaoBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

interface KakaoBookUseCase {
    suspend fun searchBook(keyword: String, page: Int): BookListEntity
}

class KakaoBookUseCaseImpl(
    private val kakaoBookRepository: KakaoBookRepository,
    private val bookMarkRepository: BookMarkRepository
) : KakaoBookUseCase {
    override suspend fun searchBook(keyword: String, page: Int): BookListEntity {
        return withContext(Dispatchers.IO) {
            val bookMarksAsync = async {
                bookMarkRepository.selectData().bookMarks
            }
            val bookResultAsync = async {
                kakaoBookRepository.getBookRepository(keyword, page)
            }
            val booksMarks = bookMarksAsync.await()
            val booksResult = bookResultAsync.await()
            booksResult.documents.map { book ->
                BookAndBookMark(book, booksMarks.find { it.title == book.title })
            }.let {
                BookListEntity(it, booksResult.meta)
            }
        }
    }
}
