package com.example.bookreport.data.entity.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.bookreport.data.entity.BookEntity
import com.example.bookreport.data.entity.Documents
import com.google.gson.Gson
import java.io.Serializable


@Entity
data class Report(
    @Embedded var book: Documents,
    var context: String,
    @PrimaryKey(autoGenerate = true)
    var no: Int = 0
): Serializable

class MyTypeConverters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String = Gson().toJson(value)

    @TypeConverter
    fun toStringList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()

}