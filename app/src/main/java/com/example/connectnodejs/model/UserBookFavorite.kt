package com.example.connectnodejs.model

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class UserBookFavorite(var id:Int?,var user_id:Int?,var book_id:Int?) {

    constructor():this(-1,-1,-1)

    companion object{
        private val gson = Gson()
        fun getAccountList(jsonElement: JsonElement): MutableList<UserBookFavorite> {
            val jsonObject = jsonElement.asJsonObject
            val userBookFavoriteList: JsonArray? = jsonObject.getAsJsonArray("userBookFavorites")
            val type = object : TypeToken<MutableList<UserBookFavorite>>() {}.type
            return gson.fromJson(userBookFavoriteList, type)
        }

        fun getAccount(jsonElement: JsonElement): UserBookFavorite? {
            val jsonObject = jsonElement.asJsonObject
            if (jsonObject.has("userBookFavorite")) {
                val userBookFavorite: JsonObject = jsonObject.getAsJsonObject("userBookFavorite")
                val type = object : TypeToken<UserBookFavorite>() {}.type
                return gson.fromJson(userBookFavorite, type)
            }
            return null
        }

        fun getLength(jsonElement: JsonElement):Int{
            return jsonElement.asJsonObject.get("length").asInt
        }

        fun getStatus(jsonElement: JsonElement):String{
            return jsonElement.asJsonObject.get("status").asString
        }

    }
}