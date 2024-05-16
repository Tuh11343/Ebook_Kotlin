package com.example.connectnodejs.network.api

import com.example.connectnodejs.model.UserBookFavorite
import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserBookFavoriteAPIService {

    @GET("/api/v1/userBookFavorite/id")
    fun findByID(@Query("id") id:Int):Single<JsonElement>
    
    @GET("/api/v1/userBookFavorite/userID")
    fun findByUserID(@Query("id") id:Int):Single<JsonElement>

    @GET("/api/v1/userBookFavorite/bookID")
    fun findByBookID(@Path("id") id:Int):Single<JsonElement>

    @PUT("/api/v1/userBookFavorite")
    fun update(@Body userBookFavorite:UserBookFavorite):Single<JsonElement>

    @POST("/api/v1/userBookFavorite")
    fun create(@Body userBookFavorite: UserBookFavorite):Single<JsonElement>
    
    @DELETE
    fun delete(@Query("id")id:Int):Single<JsonElement>
}