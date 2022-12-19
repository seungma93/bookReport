package com.example.bookreport.data.local

import android.util.Log
import com.example.bookreport.data.entity.BookMarkEntity
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.room.BookMarkDatabase
import javax.inject.Inject

interface BookMarkLocalDataSource {
    suspend fun insert(bookMark: BookMark)
    suspend fun delete(bookMark: BookMark)
    suspend fun select(): BookMarkEntity
}

class BookMarkLocalDataSourceImpl @Inject constructor(private val db: BookMarkDatabase) :
    BookMarkLocalDataSource {

    override suspend fun insert(bookMark: BookMark) {
        db.bookMarkDao().insert(bookMark)
    }

    override suspend fun delete(bookMark: BookMark) {
        db.bookMarkDao().delete(bookMark)
    }

    override suspend fun select(): BookMarkEntity {
        if (db.bookMarkDao().getAll().isNotEmpty()) {
            for (element in db.bookMarkDao().getAll()) {
                Log.v("북마크 엔티티", element.title)
            }
        } else {
            Log.v("북마크 엔티티", "X")
        }
        return BookMarkEntity(db.bookMarkDao().getAll())
    }
}

