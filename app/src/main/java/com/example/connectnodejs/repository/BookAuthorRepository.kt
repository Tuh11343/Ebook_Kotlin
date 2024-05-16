package com.example.connectnodejs.repository

import android.util.Log
import com.example.connectnodejs.network.RetrofitClient
import com.example.connectnodejs.network.api.BookAuthorAPIService
import com.google.gson.JsonElement
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class BookAuthorRepository {

    private val apiService: BookAuthorAPIService =
        RetrofitClient.get()!!.create(BookAuthorAPIService::class.java)

    fun getAllBookAuthors(limit: Int?, offset: Int?): Single<JsonElement> {
        return apiService.findAll(limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get all bookAuthors error: $throwable")
                Single.error(throwable) // Đảm bảo rằng lỗi được chuyển tiếp
            }
    }
    
}