package com.example.bookreport.data.entity.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.KakaoBookDocuments
import com.google.gson.Gson
import java.io.Serializable


@Entity
data class Report(
    @Embedded var book: KakaoBookDocuments,
    var context: String,
    @PrimaryKey(autoGenerate = true)
    var no: Int = 0
): Serializable
/*
{
    @PrimaryKey(autoGenerate = true)
    var no = 0
}
*/
class MyTypeConverters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String = Gson().toJson(value)

    @TypeConverter
    fun toStringList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}