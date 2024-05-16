package com.example.connectnodejs.network.api

import com.example.connectnodejs.model.Subscription
import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface SubscriptionAPIService {

    @GET("/api/v1/subscription/accountID")
    fun findByAccountID(@Query("id") id:Int?): Single<JsonElement>

    @POST("/api/v1/subscription")
    fun create(@Body subscription: Subscription):Single<JsonElement>

    @PUT("/api/v1/subscription")
    fun update(@Body subscription: Subscription):Single<JsonElement>
}