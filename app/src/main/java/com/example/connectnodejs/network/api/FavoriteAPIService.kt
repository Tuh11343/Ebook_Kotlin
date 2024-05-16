package com.example.connectnodejs.network.api

import com.example.connectnodejs.model.Favorite
import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FavoriteAPIService {

    @GET("/api/v1/favorite/userID")
    fun findByUserID(@Query("id") id:Int):Single<JsonElement>

    @PUT("/api/v1/favorite")
    fun updateFavorite(@Body favorite:Favorite):Single<JsonElement>

    @POST("/api/v1/favorite")
    fun createFavorite(@Body favorite: Favorite):Single<JsonElement>

    @POST("/api/v1/favorite/favoriteClick")
    fun favoriteClick(@Query("user_id")user_id:Int,@Query("book_id")book_id:Int):Single<JsonElement>

    @DELETE("/api/v1/favorite")
    fun deleteFavorite(@Query("id") id:Int):Single<JsonElement>
}