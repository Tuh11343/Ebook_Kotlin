package com.example.connectnodejs.model

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class Favorite(var id:Int?,var user_id:Int,var book_id:Int) {

    constructor():this(null,-1,-1)

    companion object{
        private val gson = Gson()
        fun getFavoriteList(jsonElement: JsonElement): MutableList<Favorite> {
            val jsonObject = jsonElement.asJsonObject
            val favoriteList: JsonArray? = jsonObject.getAsJsonArray("favorites")
            val type = object : TypeToken<MutableList<Favorite>>() {}.type
            return gson.fromJson(favoriteList, type)
        }

        fun getFavorite(jsonElement: JsonElement): Favorite {
            val jsonObject = jsonElement.asJsonObject
            val favorite: JsonObject =jsonObject.get("favorite").asJsonObject
            val type=object : TypeToken<Favorite>(){}.type
            return gson.fromJson(favorite,type)
        }

        fun getLength(jsonElement: JsonElement):Int{
            return jsonElement.asJsonObject.get("length").asInt
        }

        fun getStatus(jsonElement: JsonElement):String{
            return jsonElement.asJsonObject.get("status").asString
        }

        fun getAction(jsonElement: JsonElement):Boolean{
            return jsonElement.asJsonObject.get("action").asBoolean
        }

    }

}