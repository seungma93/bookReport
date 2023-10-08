package com.example.bookreport.data.entity

import com.example.bookreport.data.entity.room.BookMark
import org.w3c.dom.Document
import java.io.Serializable


sealed class BookListEntity {
    abstract val entities: List<BookAndBookMark>
    abstract val meta: Meta

    data class KakaoBookListEntity(
        override val entities: List<BookAndBookMark>,
        override val meta: Meta.KakaoBookMeta
    ) : BookListEntity()

    data class GoogleBooksListEntity(
        override val entities: List<BookAndBookMark>,
        override val meta: Meta.GoogleBooksMeta
    ) : BookListEntity()
}

data class BookAndBookMark(
    val bookDocuments: Documents,
    val bookMark: BookMark? = null
) : Serializable

