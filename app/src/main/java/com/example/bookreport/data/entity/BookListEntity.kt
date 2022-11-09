package com.example.bookreport.data.entity

import com.example.bookreport.data.entity.room.BookMark
import java.io.Serializable

data class BookListEntity (
    val entities: List<BookAndBookMark>,
    val meta: KakaoBookMeta?
)

data class BookAndBookMark(
    val book: KakaoBook,
    val bookMark: BookMark? = null
) : Serializable

