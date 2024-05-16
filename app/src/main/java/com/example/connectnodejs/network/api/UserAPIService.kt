package com.example.connectnodejs.network.api

import com.example.connectnodejs.model.User
import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserAPIService {

    @GET("/api/v1/user/id")
    fun findByID(@Query("id") id:Int):Single<JsonElement>

    @GET("/api/v1/user/accountID")
    fun findByAccountID(@Query("id") id:Int):Single<JsonElement>

    @PUT("/api/v1/user")
    fun updateUser(@Body user:User):Single<JsonElement>

    @POST("/api/v1/user")
    fun createUser(@Body user: User):Single<JsonElement>
}