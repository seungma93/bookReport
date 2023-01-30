package com.example.bookreport.data.local

import android.util.Log
import com.example.bookreport.data.entity.BookMarkEntity
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.room.BookMarkDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface BookMarkLocalDataSource {
    suspend fun insert(bookMark: BookMark)
    suspend fun delete(bookMark: BookMark)
    fun select(): Flow<List<BookMark>>
}

class BookMarkLocalDataSourceImpl @Inject constructor(private val db: BookMarkDatabase) :
    BookMarkLocalDataSource {

    override suspend fun insert(bookMark: BookMark) {
        db.bookMarkDao().insert(bookMark)
    }

    override suspend fun delete(bookMark: BookMark) {
        db.bookMarkDao().delete(bookMark)
    }

    override fun select(): Flow<List<BookMark>> {
        /*
        db.bookMarkDao().getAllBookMakrs().map {
            if (it.isNotEmpty()) {
                it.map {
                    Log.v("북마크 엔티티", it.title)
                }
            } else {
                Log.v("북마크 엔티티", "X")
            }
        }
        */
        Log.v("북마크 엔티티" , "끝")
        return db.bookMarkDao().getAllBookMakrs()
    }
}

