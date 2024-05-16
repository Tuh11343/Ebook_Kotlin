package com.example.connectnodejs.model

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class BookAuthor(var id:Int?,var book_id:Int,var author_id:Int) {

    constructor():this(null,-1,-1)

    companion object{
        private val gson = Gson()
        fun getBookAuthorList(jsonElement: JsonElement): MutableList<BookAuthor> {
            val jsonObject = jsonElement.asJsonObject
            val bookBookAuthorList: JsonArray? = jsonObject.getAsJsonArray("bookAuthors")
            val type = object : TypeToken<MutableList<BookAuthor>>() {}.type
            return gson.fromJson(bookBookAuthorList, type)
        }

        fun getBookAuthor(jsonElement: JsonElement): BookAuthor {
            val jsonObject = jsonElement.asJsonObject
            val bookBookAuthor: JsonObject =jsonObject.get("bookAuthor").asJsonObject
            val type=object : TypeToken<BookAuthor>(){}.type
            return gson.fromJson(bookBookAuthor,type)
        }

        fun getLength(jsonElement: JsonElement):Int{
            return jsonElement.asJsonObject.get("length").asInt
        }

        fun getStatus(jsonElement: JsonElement):String{
            return jsonElement.asJsonObject.get("status").asString
        }

    }

}