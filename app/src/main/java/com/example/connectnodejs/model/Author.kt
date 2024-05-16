package com.example.connectnodejs.model

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class Author(var id:Int?,var name:String,var image:String?,var description:String?) {

    constructor():this(null,"",null,null)

    companion object{
        private val gson = Gson()
        fun getAuthorList(jsonElement: JsonElement): MutableList<Author> {
            val jsonObject = jsonElement.asJsonObject
            val authorList: JsonArray? = jsonObject.getAsJsonArray("authors")
            val type = object : TypeToken<MutableList<Author>>() {}.type
            return gson.fromJson(authorList, type)
        }

        fun getAuthor(jsonElement: JsonElement): Author {
            val jsonObject = jsonElement.asJsonObject
            val author: JsonObject =jsonObject.get("author").asJsonObject
            val type=object : TypeToken<Author>(){}.type
            return gson.fromJson(author,type)
        }

        fun getAuthorName(jsonElement: JsonElement): String {
            return jsonElement.asJsonObject.get("author").asString
        }

        fun getLength(jsonElement: JsonElement):Int{
            return jsonElement.asJsonObject.get("length").asInt
        }

        fun getStatus(jsonElement: JsonElement):String{
            return jsonElement.asJsonObject.get("status").asString
        }

    }

}