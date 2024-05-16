package com.example.connectnodejs.network.api

import com.example.connectnodejs.model.Genre
import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface BookAPIService {

    @GET("/api/v1/book")
    fun findAll(@Query("limit") limit:Int?, @Query("offset") offset:Int?): Single<JsonElement>

    @GET("/api/v1/book/genre")
    fun findByGenreID(@Query("id") id:Int,@Query("limit") limit:Int?, @Query("offset") offset:Int?): Single<JsonElement>

    @GET("/api/v1/book/author")
    fun findByAuthorID(@Query("id") id:Int,@Query("limit") limit:Int?, @Query("offset") offset:Int?): Single<JsonElement>

    @GET("/api/v1/book/author")
    fun findByAuthorID(@Query("id") id:Int): Single<JsonElement>

    @GET("/api/v1/book/normal")
    fun findNormalBook(@Query("limit") limit:Int?, @Query("offset") offset:Int?): Single<JsonElement>

    @GET("/api/v1/book/premium")
    fun findPremiumBook(@Query("limit") limit:Int?, @Query("offset") offset:Int?): Single<JsonElement>

    @GET("/api/v1/book/favorite")
    fun findByFavorite(@Query("id") id:Int): Single<JsonElement>

    @GET("/api/v1/book/nameAndGenre")
    fun findByNameAndGenre(@Query("name") name:String,@Query("genre_id") genreID:Int,@Query("limit") limit:Int?, @Query("offset") offset:Int?): Single<JsonElement>

    @GET("/api/v1/book/name")
    fun findByName(@Query("name") name:String,@Query("limit") limit:Int?, @Query("offset") offset:Int?): Single<JsonElement>

    @GET("/api/v1/book/topRating")
    fun findByTopRating(@Query("limit") limit:Int?,@Query("offset") offset:Int?): Single<JsonElement>

}