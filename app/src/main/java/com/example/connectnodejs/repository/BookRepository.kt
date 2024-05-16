package com.example.connectnodejs.repository

import android.util.Log
import com.example.connectnodejs.model.Genre
import com.example.connectnodejs.network.RetrofitClient
import com.example.connectnodejs.network.api.BookAPIService
import com.google.gson.JsonElement
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class BookRepository {

    private val apiService: BookAPIService =
        RetrofitClient.get()!!.create(BookAPIService::class.java)

    fun findAll(limit: Int?, offset: Int?): Single<JsonElement> {
        return apiService.findAll(limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get all book error: $throwable")
                Single.error(throwable)
            }
    }

    fun findNormalBook(limit: Int?, offset: Int?): Single<JsonElement> {
        return apiService.findNormalBook(limit,offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get all book error: $throwable")
                Single.error(throwable)
            }
    }

    fun findPremiumBook(limit: Int?, offset: Int?): Single<JsonElement> {
        return apiService.findPremiumBook(limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get all book error: $throwable")
                Single.error(throwable)
            }
    }


    fun findByGenreID(id:Int,limit: Int?, offset: Int?): Single<JsonElement> {
        return apiService.findByGenreID(id,limit,offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get all book error: $throwable")
                Single.error(throwable)
            }
    }

    fun findByAuthorID(id:Int,limit: Int?, offset: Int?): Single<JsonElement> {
        return apiService.findByAuthorID(id,limit,offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get all book error: $throwable")
                Single.error(throwable)
            }
    }

    fun findByAuthorID(id:Int): Single<JsonElement> {
        return apiService.findByAuthorID(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get all book error: $throwable")
                Single.error(throwable)
            }
    }

    fun findByFavorite(id:Int): Single<JsonElement> {
        return apiService.findByFavorite(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get all book error: $throwable")
                Single.error(throwable)
            }
    }

    fun findByNameAndGenre(name:String,genreID:Int,limit: Int?,offset: Int?): Single<JsonElement> {
        return apiService.findByNameAndGenre(name,genreID,limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get all book by name and genre error: $throwable")
                Single.error(throwable)
            }
    }

    fun findByName(name:String,limit: Int?,offset: Int?): Single<JsonElement> {
        return apiService.findByName(name, limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get all book by name error: $throwable")
                Single.error(throwable)
            }
    }

    fun findByTopRating(limit: Int?,offset: Int?): Single<JsonElement> {
        return apiService.findByTopRating(limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get all book by top rating error: $throwable")
                Single.error(throwable)
            }
    }

}