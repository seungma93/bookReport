package com.example.bookreport.repository

import com.example.bookreport.BookResponse
import com.example.bookreport.ResultSearchKeywordResponse
import com.example.bookreport.data.mapper.toEntity
import com.example.bookreport.data.remote.KakaoRemoteDataSource
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class KakaoBookRepositoryImplTest {

    private lateinit var dataSource: KakaoRemoteDataSource
    private lateinit var repository: KakaoBookRepositoryImpl

    private val bookResponse1 = mock<BookResponse> {
        on { contents } doReturn "컨텐츠"
    }

    @Before
    fun setUp() {
        dataSource = mock()
        repository = KakaoBookRepositoryImpl(dataSource)
    }

    @Test
    fun `Book load 테스트`() = runTest {
        // given
        /*whenever(bookResponse1.title).thenReturn("제목1")
        val response = mock<ResultSearchKeywordResponse> {
            on { meta } doReturn mock()
            on { documents } doReturn listOf(bookResponse1)
        }*/
        whenever(dataSource.getSearchKeyword(any(), any()))
            .thenReturn(
                ResultSearchKeywordResponse(
                    meta = mock(),
                    documents = listOf(
                        BookResponse(title = "제목1")
                    )
                )
            )

        // when
        val result = repository.getBookRepository("", 0)

        // then
        result.let {
            assert(it.documents[0].title == "제목1")
            assert(it.documents.size == 1)
        }
    }
}