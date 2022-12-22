package com.example.bookreport.domain

import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.repository.BookMarkRepository
import com.example.bookreport.repository.KakaoBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface KakaoBookUseCase {
    suspend fun searchBook(keyword: String, page: Int): BookListEntity.KakaoBookBookListEntity
    suspend fun refreshBookMark(entity: BookListEntity.KakaoBookBookListEntity): BookListEntity.KakaoBookBookListEntity
}

class KakaoBookUseCaseImpl @Inject constructor(
    private val kakaoBookRepository: KakaoBookRepository,
    private val bookMarkRepository: BookMarkRepository
) : KakaoBookUseCase {
    override suspend fun searchBook(keyword: String, page: Int): BookListEntity.KakaoBookBookListEntity {
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
                BookAndBookMark.KakaoBook(book, booksMarks.find { it.title == book.title })
            }.let {
                BookListEntity.KakaoBookBookListEntity(it, booksResult.meta)
            }
        }
    }

    override suspend fun refreshBookMark(entity: BookListEntity.KakaoBookBookListEntity): BookListEntity.KakaoBookBookListEntity {
        val bookMark = bookMarkRepository.selectData().bookMarks

        return entity.entities.map { bookAndBookMark ->
            BookAndBookMark.KakaoBook( bookAndBookMark.book, bookMark.find{ it.title == bookAndBookMark.book.title})
        }.let{
            BookListEntity.KakaoBookBookListEntity(it, entity.meta)
        }
    }
}
