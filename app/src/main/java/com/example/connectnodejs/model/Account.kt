package com.example.connectnodejs.model

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class Account(var id:Int?, var user_id:Int, var subscription_id:Int, var email:String, var password:String, var is_verified:Boolean) {

    constructor():this(null,-1,-1,"","",false)

    companion object{
        private val gson = Gson()
        fun getAccountList(jsonElement: JsonElement): MutableList<Account> {
            val jsonObject = jsonElement.asJsonObject
            val accountList: JsonArray? = jsonObject.getAsJsonArray("accounts")
            val type = object : TypeToken<MutableList<Account>>() {}.type
            return gson.fromJson(accountList, type)
        }

        fun getAccount(jsonElement: JsonElement): Account? {
            val jsonObject = jsonElement.asJsonObject
            if (jsonObject.has("account")) {
                val account: JsonObject = jsonObject.getAsJsonObject("account")
                val type = object : TypeToken<Account>() {}.type
                return gson.fromJson(account, type)
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