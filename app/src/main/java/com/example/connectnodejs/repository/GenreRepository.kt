package com.example.connectnodejs.repository

import android.util.Log
import com.example.connectnodejs.network.RetrofitClient
import com.example.connectnodejs.network.api.GenreAPIService
import com.google.gson.JsonElement
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class GenreRepository {
    private val apiService: GenreAPIService =
        RetrofitClient.get()!!.create(GenreAPIService::class.java)

    fun getAllGenres(limit: Int?, offset: Int?): Single<JsonElement> {
        return apiService.findAll(limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get all genres error: $throwable")
                Single.error(throwable) // Đảm bảo rằng lỗi được chuyển tiếp
            }
    }

    fun findByBookID(id:Int,limit: Int?, offset: Int?): Single<JsonElement> {
        return apiService.findByBookID(id,limit,offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get all genres error: $throwable")
                Single.error(throwable) // Đảm bảo rằng lỗi được chuyển tiếp
            }
    }



}