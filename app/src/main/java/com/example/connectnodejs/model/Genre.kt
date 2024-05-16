package com.example.connectnodejs.model

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class Genre(var id: Int, var name: String,var image:String?) {
    constructor(): this(-1, "",null)

    override fun toString(): String {
        return "ID: $id, Name: $name"
    }

    companion object{
        private val gson = Gson()
        fun getGenreList(jsonElement: JsonElement): MutableList<Genre> {
            val jsonObject = jsonElement.asJsonObject
            val genreList: JsonArray? = jsonObject.getAsJsonArray("genres")
            val type = object : TypeToken<MutableList<Genre>>() {}.type
            return gson.fromJson(genreList, type)
        }

        fun getGenre(jsonElement: JsonElement): Genre {
            val jsonObject = jsonElement.asJsonObject
            val genre:JsonObject=jsonObject.get("genre").asJsonObject
            val type=object :TypeToken<Genre>(){}.type
            return gson.fromJson(genre,type)
        }

        fun getLength(jsonElement: JsonElement):Int{
            /*val jsonObject = jsonElement.asJsonObject
            val length:JsonObject=jsonObject.get("length").asJsonObject
            val type=object :TypeToken<Int>(){}.type
            return gson.fromJson(length,type)*/

            return jsonElement.asJsonObject.get("length").asInt
        }

        fun getStatus(jsonElement: JsonElement):String{
            /*val jsonObject = jsonElement.asJsonObject
            val status:JsonObject=jsonObject.get("status").asJsonObject
            val type=object :TypeToken<String>(){}.type
            return gson.fromJson(String,type)*/
            return jsonElement.asJsonObject.get("status").asString
        }

    }
}