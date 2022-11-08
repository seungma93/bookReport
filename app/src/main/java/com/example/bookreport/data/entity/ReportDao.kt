package com.example.bookreport.data.entity

import androidx.room.*


@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: Report)

    @Update
    suspend fun update(report: Report)

    @Delete
    suspend fun delete(report: Report)


    @Query("SELECT * FROM Report")
    suspend fun getAll(): List<Report>
/*
    @Query("DELETE FROM User ")
    suspend fun deleteAll()
     */
}