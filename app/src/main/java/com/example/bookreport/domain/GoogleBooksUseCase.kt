package com.example.bookreport.domain

import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.repository.BookMarkRepository
import com.example.bookreport.repository.GoogleBooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

interface GoogleBooksUseCase {
    suspend fun searchBook(keyword: String, page: Int): BookListEntity.GoogleBooksBookListEntity
    suspend fun refreshBookMark(entity: BookListEntity.GoogleBooksBookListEntity): BookListEntity.GoogleBooksBookListEntity
}

class GoogleBooksUseCaseImpl (
    private val GoogleBooksRepository: GoogleBooksRepository,
    private val bookMarkRepository: BookMarkRepository
) : GoogleBooksUseCase {
    override suspend fun searchBook(keyword: String, startIndex:Int): BookListEntity.GoogleBooksBookListEntity {
        return withContext(Dispatchers.IO) {
            val bookMarksAsync = async {
                bookMarkRepository.selectData().bookMarks
            }
            val bookResultAsync = async {
                GoogleBooksRepository.getGoogleBooksEntity(keyword, startIndex)
            }
            val booksMarks = bookMarksAsync.await()
            val booksResult = bookResultAsync.await()

            booksResult.items.map { book ->
                BookAndBookMark.GoogleBooks(book, booksMarks.find { it.title == book.volumeInfo?.title })
            }.let {
                BookListEntity.GoogleBooksBookListEntity(it, booksResult.meta)
            }
        }
    }

    override suspend fun refreshBookMark(entity: BookListEntity.GoogleBooksBookListEntity): BookListEntity.GoogleBooksBookListEntity {
        val bookMark = bookMarkRepository.selectData().bookMarks

        return entity.entities.map { bookAndBookMark ->
            BookAndBookMark.GoogleBooks( bookAndBookMark.book, bookMark.find{ it.title == bookAndBookMark.book.volumeInfo?.title})
        }.let{
            BookListEntity.GoogleBooksBookListEntity(it, entity.meta)
        }
    }
}
