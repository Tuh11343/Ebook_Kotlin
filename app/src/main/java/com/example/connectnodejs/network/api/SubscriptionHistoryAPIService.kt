package com.example.connectnodejs.network.api

import com.example.connectnodejs.model.SubscriptionHistory
import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface SubscriptionHistoryAPIService {

    @GET("/api/v1/subscriptionHistory")
    fun findByID(@Query("id") id:Int):Single<JsonElement>

    @GET("/api/v1/subscriptionHistory/subscriptionID")
    fun findBySubscriptionID(@Query("id") id:Int):Single<JsonElement>

    @POST("/api/v1/subscriptionHistory")
    fun createSubscriptionHistory(@Body subscriptionHistory: SubscriptionHistory):Single<JsonElement>

    @PUT("/api/v1/subscriptionHistory")
    fun updateSubscriptionHistory(@Body subscriptionHistory: SubscriptionHistory):Single<JsonElement>
}