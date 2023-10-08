package com.example.bookreport.repository

import com.example.bookreport.data.entity.BookEntity
import com.example.bookreport.data.mapper.toEntity
import com.example.bookreport.data.remote.GoogleBooksRemoteDataSource
import com.example.bookreport.data.remote.KakaoBookRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject


interface BookDataSourceWrapper {
    suspend fun getDataSource(keyword: String, page: Int): Flow<BookEntity?>
    fun subscribeDataSource(): Flow<BookEntity?>
}

class KakaoBookDataSourceWrapperImpl @Inject constructor(
    private val kakaoBookRemoteDataSource: KakaoBookRemoteDataSource
) : BookDataSourceWrapper {
    private val bookEntityStateFlow = MutableStateFlow<BookEntity?>(null)

    private var keyword: String? = null

    override suspend fun getDataSource(keyword: String, page: Int): Flow<BookEntity?> {

        return when {
            this.keyword == null -> {
                this.keyword = keyword
                bookEntityStateFlow.value = null
                kakaoBookRemoteDataSource.getSearchKeyword(keyword, page).toEntity().run {
                    val newList = copy(items = items.filterNot { it.isbn.isEmpty() })
                    bookEntityStateFlow.value =
                        BookEntity.KakaoBookEntity(newList.meta, newList.items)
                    bookEntityStateFlow
                }
            }
            this.keyword == keyword -> {
                when (page) {
                    1 -> {
                        bookEntityStateFlow.value = null
                        kakaoBookRemoteDataSource.getSearchKeyword(keyword, page).toEntity().run {
                            val newList = copy(items = items.filterNot { it.isbn.isEmpty() })
                            bookEntityStateFlow.value =
                                BookEntity.KakaoBookEntity(newList.meta, newList.items)
                            bookEntityStateFlow
                        }
                    }
                    else -> {
                        val oldList = bookEntityStateFlow.value as BookEntity.KakaoBookEntity
                        kakaoBookRemoteDataSource.getSearchKeyword(keyword, page).toEntity().run {
                            val newList = copy(items = items.filterNot { it.isbn.isEmpty() })
                            bookEntityStateFlow.value =
                                BookEntity.KakaoBookEntity(
                                    newList.meta,
                                    oldList.items + newList.items
                                )
                            bookEntityStateFlow
                        }
                    }
                }
            }
            else -> {
                this.keyword = keyword
                bookEntityStateFlow.value = null
                kakaoBookRemoteDataSource.getSearchKeyword(keyword, page).toEntity().run {
                    val newList = copy(items = items.filterNot { it.isbn.isEmpty() })
                    bookEntityStateFlow.value =
                        BookEntity.KakaoBookEntity(newList.meta, newList.items)
                    bookEntityStateFlow
                }
            }
        }

    }


    override fun subscribeDataSource(): Flow<BookEntity> = bookEntityStateFlow.filterNotNull()
}

class GoogleBooksDataSourceWrapperImpl @Inject constructor(
    private val googleBooksRemoteDataSource: GoogleBooksRemoteDataSource
) : BookDataSourceWrapper {
    private val bookEntityStateFlow = MutableStateFlow<BookEntity?>(null)
    private var keyword: String? = null

    override suspend fun getDataSource(keyword: String, page: Int): Flow<BookEntity?> {
        val startIndex = 0 + 10 * (page - 1)

        return when {
            this.keyword == null -> {
                this.keyword = keyword
                bookEntityStateFlow.value = null
                googleBooksRemoteDataSource.getSearchKeyword(keyword, startIndex, "partial")
                    .toEntity()
                    .run {
                        val newList = copy(items = items.filterNot { it.isbn.isEmpty() })
                        bookEntityStateFlow.value =
                            BookEntity.GoogleBooksEntity(
                                newList.meta,
                                newList.items
                            )
                        bookEntityStateFlow
                    }
            }
            this.keyword == keyword -> {
                when (page) {
                    1 -> {
                        bookEntityStateFlow.value = null
                        googleBooksRemoteDataSource.getSearchKeyword(keyword, startIndex, "partial")
                            .toEntity()
                            .run {
                                val newList = copy(items = items.filterNot { it.isbn.isEmpty() })
                                bookEntityStateFlow.value =
                                    BookEntity.GoogleBooksEntity(
                                        newList.meta,
                                        newList.items
                                    )
                                bookEntityStateFlow
                            }
                    }
                    else -> {
                        val oldList = bookEntityStateFlow.value as BookEntity.GoogleBooksEntity
                        googleBooksRemoteDataSource.getSearchKeyword(keyword, startIndex, "partial")
                            .toEntity()
                            .run {
                                val newList = copy(items = items.filterNot { it.isbn.isEmpty() })
                                bookEntityStateFlow.value =
                                    BookEntity.GoogleBooksEntity(
                                        newList.meta,
                                        oldList.items + newList.items
                                    )
                                bookEntityStateFlow
                            }
                    }
                }
            }
            else -> {
                this.keyword = keyword
                bookEntityStateFlow.value = null
                googleBooksRemoteDataSource.getSearchKeyword(keyword, startIndex, "partial")
                    .toEntity()
                    .run {
                        val newList = copy(items = items.filterNot { it.isbn.isEmpty() })
                        bookEntityStateFlow.value =
                            BookEntity.GoogleBooksEntity(
                                newList.meta,
                                newList.items
                            )
                        bookEntityStateFlow
                    }
            }
        }
    }

    override fun subscribeDataSource(): Flow<BookEntity?> = bookEntityStateFlow
}

