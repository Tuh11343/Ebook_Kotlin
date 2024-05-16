package com.example.connectnodejs.network.api

import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthorAPIService {

    @GET("/api/v1/author")
    fun findAll(@Query("limit") limit:Int?, @Query("offset") offset:Int?): Single<JsonElement>

    @GET("/api/v1/author/bookID")
    fun findByBookID(@Query("id") id:Int,@Query("limit") limit:Int?, @Query("offset") offset:Int?):Single<JsonElement>

    @GET("/api/v1/author/bookAuthorOne")
    fun findOneByBookID(@Query("id") id:Int):Single<JsonElement>
}