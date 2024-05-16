package com.example.connectnodejs.network

import com.google.gson.GsonBuilder
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient {
    companion object {
        private var retrofit: Retrofit? = null
        private const val BASE_URL = "http://192.168.1.195:5000"

        fun get(): Retrofit {
            if (retrofit == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()

                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

                /*var client: OkHttpClient? = null
                if(token!=null){
                    client = OkHttpClient.Builder().apply {
                        addInterceptor { chain ->
                            val originalRequest: Request = chain.request()

                            val requestWithToken: Request = originalRequest.newBuilder()
                                .header("Authorization", "Bearer $token")
                                .build()

                            chain.proceed(requestWithToken)
                        }
                    }.build()
                }*/

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()


            }
            return retrofit!!
        }

        fun dismiss(){
            retrofit=null
        }

    }
}
