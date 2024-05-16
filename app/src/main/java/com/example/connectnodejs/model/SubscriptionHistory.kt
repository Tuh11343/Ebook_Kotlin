package com.example.connectnodejs.model

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.util.Calendar
import java.util.Date

class SubscriptionHistory(var id:Int?, var name:String, var price:Float, var start: Date, var end:Date) {

    constructor():this(null,"",0f, Calendar.getInstance().time, Calendar.getInstance().time)

    companion object{
        private val gson = Gson()
        fun getSubscriptionList(jsonElement: JsonElement): MutableList<SubscriptionHistory> {
            val jsonObject = jsonElement.asJsonObject
            val subscriptionList: JsonArray? = jsonObject.getAsJsonArray("subscriptionHistories")
            val type = object : TypeToken<MutableList<SubscriptionHistory>>() {}.type
            return gson.fromJson(subscriptionList, type)
        }

        fun getSubscriptionHistory(jsonElement: JsonElement): SubscriptionHistory {
            val jsonObject = jsonElement.asJsonObject
            val subscription: JsonObject =jsonObject.get("subscriptionHistory").asJsonObject
            val type=object : TypeToken<SubscriptionHistory>(){}.type
            return gson.fromJson(subscription,type)
        }

        fun getLength(jsonElement: JsonElement):Int{
            return jsonElement.asJsonObject.get("length").asInt
        }

        fun getStatus(jsonElement: JsonElement):String{
            return jsonElement.asJsonObject.get("status").asString
        }

    }

}