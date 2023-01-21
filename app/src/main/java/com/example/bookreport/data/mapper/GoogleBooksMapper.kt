package com.example.bookreport.data.mapper

import android.util.Log
import com.example.bookreport.data.entity.*
import com.example.bookreport.data.entity.response.GoogleBooksItemsResponse
import com.example.bookreport.data.entity.response.GoogleBooksResponse
import com.example.bookreport.data.entity.response.Isbn

fun GoogleBooksResponse.toEntity(): BookEntity.GoogleBooksEntity {
    Log.v("toEntity", "시작")
    return BookEntity.GoogleBooksEntity(
        meta = Meta.GoogleBooksMeta(
            totalCount = totalItems ?: 0,
            bookItemSize = items.size
        ),
        items = items.map {
            it.toDocument()
        }
    )
}

fun GoogleBooksItemsResponse.toDocument(): Documents {
    Log.v("toDocuments", "시작")
    val a = Documents(
        title = volumeInfo?.title.orEmpty(),
        contents = volumeInfo?.description.orEmpty(),
        url = selfLink.orEmpty(),
        isbn = volumeInfo
            ?.industryIdentifiers
            ?.find {
                Isbn.values().map { it.value }.contains(it.type)
            }
            ?.identifier
            ?: "없음",
        datetime = volumeInfo?.publishedDate.orEmpty(),
        authors = volumeInfo?.authors.orEmpty(),
        publisher = volumeInfo?.publisher.orEmpty(),
        thumbnail = volumeInfo?.imageLinks?.smallThumbnail.orEmpty()
    )
    Log.v("toDocuments", a.isbn)

    return a
}

fun List<BookAndBookMark>.toBookMark(entity: BookListEntity): BookListEntity {
    return when(entity){
        is BookListEntity.KakaoBookListEntity -> BookListEntity.KakaoBookListEntity(this, entity.meta)
        is BookListEntity.GoogleBooksListEntity -> BookListEntity.GoogleBooksListEntity(this, entity.meta)
    }
}
