package com.example.bookreport.data.mapper

import com.example.bookreport.*
import com.example.bookreport.data.entity.*

fun KakaoBookResponse.toEntity() = BookEntity.KakaoBookEntity(
    meta = meta.toEntity(),
    documents = documents.map {
        it.toEntity()
    }
)

fun KakaoBookMetaResponse.toEntity() = KakaoBookMeta(
    totalCount = totalCount ?: 0,
    pageableCount = pageableCount ?: 0,
    isEnd = isEnd ?: true
)

fun KakaoBookDocumentsResponse.toEntity() = KakaoBookDocuments(
    title = title.orEmpty(),
    contents = contents.orEmpty(),
    url = url.orEmpty(),
    isbn = isbn.orEmpty(),
    datetime = datetime.orEmpty(),
    authors = authors.orEmpty(),
    publisher = publisher.orEmpty(),
    translators = translators.orEmpty(),
    price = price ?: 0,
    salePrice = sale_price ?: 0,
    thumbnail = thumbnail.orEmpty(),
    status = status.orEmpty()
)