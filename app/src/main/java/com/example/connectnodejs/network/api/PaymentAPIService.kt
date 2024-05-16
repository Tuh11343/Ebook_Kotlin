package com.example.connectnodejs.network.api

import com.example.connectnodejs.model.Account
import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface PaymentAPIService {

    @POST("/api/v1/payment")
    fun paymentRequest(@Query("accountID") accountID:Int,@Query("total") total:Float):Single<JsonElement>

}