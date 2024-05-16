package com.example.connectnodejs.repository

import android.util.Log
import com.example.connectnodejs.model.Favorite
import com.example.connectnodejs.network.RetrofitClient
import com.example.connectnodejs.network.api.FavoriteAPIService
import com.google.gson.JsonElement
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class FavoriteRepository {

    private val apiService: FavoriteAPIService =
        RetrofitClient.get()!!.create(FavoriteAPIService::class.java)

    fun findByUserID(id:Int): Single<JsonElement> {
        return apiService.findByUserID(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Find favorite by user id error: $throwable")
                Single.error(throwable)
            }
    }

    fun delete(id:Int): Single<JsonElement> {
        return apiService.deleteFavorite(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Delete favorite by id error: $throwable")
                Single.error(throwable)
            }
    }

    fun update(favorite:Favorite): Single<JsonElement> {
        return apiService.updateFavorite(favorite)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Update favorite error: $throwable")
                Single.error(throwable)
            }
    }

    fun create(favorite:Favorite): Single<JsonElement> {
        return apiService.createFavorite(favorite)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Create favorite error: $throwable")
                Single.error(throwable)
            }
    }

    fun favoriteClick(user_id:Int,book_id:Int): Single<JsonElement> {
        return apiService.favoriteClick(user_id,book_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Create favorite error: $throwable")
                Single.error(throwable)
            }
    }

}