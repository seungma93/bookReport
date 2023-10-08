package com.example.bookreport.repository

import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.room.BookMarkDatabase
import com.example.bookreport.data.local.BookMarkLocalDataSource
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class BookMarkRepositoryImplTest {

    private lateinit var db: BookMarkDatabase
    private lateinit var dataSource: BookMarkLocalDataSource
    private lateinit var repository: BookMarkRepositoryImpl


    @Before
    fun setUp() {
        db =
        dataSource =
        repository = BookMarkRepositoryImpl(dataSource)
    }

    @Test
    fun `데이터 주입 테스트`() = runTest {
        //given
        val bookMark = BookMark("안녕")
        dataSource.insert(bookMark)
        //when
        val result = repository.selectData()
        //then
        assert(result.bookMarks[0].title == "안녕")
    }
}