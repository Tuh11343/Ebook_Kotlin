package com.example.connectnodejs.network.api

import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GenreAPIService {
    @GET("/api/v1/genre/id")
    fun findByID(@Query("id") id:Int):Call<JsonElement>

    @GET("/api/v1/genre")
    fun findAll(@Query("limit") limit:Int?, @Query("offset") offset:Int?): Single<JsonElement>

    @GET("/api/v1/genre/bookID")
    fun findByBookID(@Query("id") id:Int,@Query("limit") limit:Int?, @Query("offset") offset:Int?): Single<JsonElement>

}