package com.example.connectnodejs.network.api

import com.example.connectnodejs.model.Account
import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AccountAPIService {

    @GET("/api/v1/account/id")
    fun findByID(@Query("id") id:Int):Single<JsonElement>

    @GET("/api/v1/account/email")
    fun findByEmail(@Query("email") email: String):Single<JsonElement>

    @GET("/api/v1/account/signIn")
    fun signIn(@Query("email") email: String,@Query("password") password:String):Single<JsonElement>

    @PUT("/api/v1/account")
    fun updateAccount(@Body account:Account):Single<JsonElement>

    @POST("/api/v1/account")
    fun createAccount(@Body account: Account):Single<JsonElement>
}