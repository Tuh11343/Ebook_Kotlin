package com.example.connectnodejs.model

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class Subscription(
    var id: Int?,
    var subscription_history_id: Int,
    var duration: Int,
    var book_type: Book.BookType,
    var type: String,
    var price_per_month: Float,
    var limit_book_mark:Int,
) {

    constructor() : this(null, -1, -1, Book.BookType.NORMAL, "", -1F,-1)

    companion object {
        private val gson = Gson()
        fun getSubscriptionList(jsonElement: JsonElement): MutableList<Subscription> {
            val jsonObject = jsonElement.asJsonObject
            val subscriptionList: JsonArray? = jsonObject.getAsJsonArray("subscriptions")
            val type = object : TypeToken<MutableList<Subscription>>() {}.type
            return gson.fromJson(subscriptionList, type)
        }

        fun getSubscription(jsonElement: JsonElement): Subscription {
            val jsonObject = jsonElement.asJsonObject
            val subscription: JsonObject = jsonObject.get("subscription").asJsonObject
            val type = object : TypeToken<Subscription>() {}.type
            return gson.fromJson(subscription, type)
        }

        fun getLength(jsonElement: JsonElement): Int {
            return jsonElement.asJsonObject.get("length").asInt
        }

        fun getStatus(jsonElement: JsonElement): String {
            return jsonElement.asJsonObject.get("status").asString
        }

    }

}