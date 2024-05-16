package com.example.connectnodejs.repository

import android.util.Log
import com.example.connectnodejs.network.RetrofitClient
import com.example.connectnodejs.network.api.AuthorAPIService
import com.google.gson.JsonElement
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class AuthorRepository {

    private val apiService: AuthorAPIService =
        RetrofitClient.get()!!.create(AuthorAPIService::class.java)

    fun getAllAuthor(limit: Int?, offset: Int?): Single<JsonElement> {
        return apiService.findAll(limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get all author error: $throwable")
                Single.error(throwable)
            }
    }

    fun findByBookID(id:Int,limit: Int?, offset: Int?):Single<JsonElement>{
        return apiService.findByBookID(id,limit,offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Find Authors By BookID error: $throwable")
                Single.error(throwable)
            }
    }

    fun findOneByBookID(id:Int):Single<JsonElement>{
        return apiService.findOneByBookID(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Find One Author By BookID error: $throwable")
                Single.error(throwable)
            }
    }

}