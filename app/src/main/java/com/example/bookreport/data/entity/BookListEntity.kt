package com.example.bookreport.data.entity

import com.example.bookreport.data.entity.room.BookMark
import java.io.Serializable



sealed class BookListEntity{
    data class GoogleBooksBookListEntity (
        val entities: List<BookAndBookMark.GoogleBooks>,
        val meta: GoogleBooksMeta?
    )
    data class KakaoBookBookListEntity (
        val entities: List<BookAndBookMark.KakaoBook>,
        val meta: KakaoBookMeta?
    )
}

sealed class BookAndBookMark {
    data class KakaoBook(
        val book: KakaoBookDocuments,
        val bookMark: BookMark? = null
    ) : Serializable

    data class GoogleBooks(
        val book: GoogleBooksItems,
        val bookMark: BookMark? = null
    ) : Serializable
}