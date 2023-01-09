package com.example.bookreport.data.mapper

import com.example.bookreport.*
import com.example.bookreport.data.entity.*

fun KakaoBookResponse.toEntity() = BookEntity.KakaoBookEntity(
    meta = meta.toMeta(),
    items = documents.map {
        it.toDocument()
    }
)

fun KakaoBookMetaResponse.toMeta() = Meta.KakaoBookMeta(
    totalCount = totalCount ?: 0,
    isEnd = isEnd ?: true
)

fun KakaoBookDocumentsResponse.toDocument() = Documents(
    title = title.orEmpty(),
    contents = contents.orEmpty(),
    url = url.orEmpty(),
    isbn = isbn.orEmpty(),
    datetime = datetime.orEmpty(),
    authors = authors.orEmpty(),
    publisher = publisher.orEmpty(),
    thumbnail = thumbnail.orEmpty()
)