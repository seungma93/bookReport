package com.example.bookreport.data.entity.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface BookMarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookMark: BookMark)

    @Update
    suspend fun update(bookMark: BookMark)

    @Delete
    suspend fun delete(bookMark: BookMark)


    @Query("SELECT * FROM BookMark")
    fun getAllBookMakrs(): Flow<List<BookMark>>
/*
    @Query("DELETE FROM User ")
    suspend fun deleteAll()
     */
}
