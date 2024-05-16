package com.example.connectnodejs.model

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class User(var id:Int?, var name:String, var phoneNumber:String?, var address:String?) {

    constructor():this(null,"",null,null)

    companion object{
        private val gson = Gson()
        fun getUserList(jsonElement: JsonElement): MutableList<User> {
            val jsonObject = jsonElement.asJsonObject
            val userList: JsonArray? = jsonObject.getAsJsonArray("users")
            val type = object : TypeToken<MutableList<User>>() {}.type
            return gson.fromJson(userList, type)
        }

        fun getUser(jsonElement: JsonElement): User {
            val jsonObject = jsonElement.asJsonObject
            val user: JsonObject =jsonObject.get("user").asJsonObject
            val type=object : TypeToken<User>(){}.type
            return gson.fromJson(user,type)
        }

        fun getLength(jsonElement: JsonElement):Int{
            return jsonElement.asJsonObject.get("length").asInt
        }

        fun getStatus(jsonElement: JsonElement):String{
            return jsonElement.asJsonObject.get("status").asString
        }

    }
}