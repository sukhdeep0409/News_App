package com.example.newsapi.database

import androidx.room.TypeConverter
import com.example.newsapi.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String = source.name

    @TypeConverter
    fun toSource(name: String): Source = Source(name, name)
}