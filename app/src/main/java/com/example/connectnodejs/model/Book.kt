package com.example.connectnodejs.model

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class Book(
    var id: Int,
    var name: String,
    var description: String?,
    var rating: Int?,
    var progress: Float?,
    var published_year: Int,
    var image: String?,
    var language: String?,
    var book_type: BookType,
    var src_audio: String,
    var lyric: String?,
    ) {

    constructor() : this(-1, "", "", null, null, -1, null, null, BookType.NORMAL, "", null)

    companion object {
        private val gson = Gson()
        fun getBookList(jsonElement: JsonElement): MutableList<Book> {
            val jsonObject = jsonElement.asJsonObject
            val bookList: JsonArray? = jsonObject.getAsJsonArray("books")
            val type = object : TypeToken<MutableList<Book>>() {}.type
            return gson.fromJson(bookList, type)
        }

        fun getBook(jsonElement: JsonElement): Book {
            val jsonObject = jsonElement.asJsonObject
            val book: JsonObject = jsonObject.get("book").asJsonObject
            val type = object : TypeToken<Book>() {}.type
            return gson.fromJson(book, type)
        }

        fun getLength(jsonElement: JsonElement): Int {
            return jsonElement.asJsonObject.get("length").asInt
        }

        fun getStatus(jsonElement: JsonElement): String {
            return jsonElement.asJsonObject.get("status").asString
        }

    }

    enum class BookType{
        NORMAL,PREMIUM
    }

}