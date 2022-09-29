package com.lucioaguiar.imgur.rest

import com.lucioaguiar.imgur.BuildConfig
import com.lucioaguiar.imgur.data.models.Data
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException

interface RetrofitService {

    @GET("gallery/search/{page}")
    suspend fun galleries(
        @Path("page") id: Int,
        @Query("q") query: String,
        @Query("q_type") type: String,
        @Query("q_size_px") size: String
    ): Data

    companion object {

        private val retrofitService: RetrofitService by lazy {
            val retrofit = Retrofit.Builder()
                .client(httpClient())
                .baseUrl("https://api.imgur.com/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(RetrofitService::class.java)
        }

        class BasicAuthInterceptor : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val newUrl =
                    request.url().newBuilder().addQueryParameter("client_id", BuildConfig.CLIENT_ID)
                        .build()
                val newRequest = request.newBuilder().url(newUrl).build()
                return chain.proceed(newRequest)
            }
        }

        private fun httpClient(): OkHttpClient {
            val clientBuilder = OkHttpClient.Builder()

            clientBuilder.apply {
                readTimeout(1, java.util.concurrent.TimeUnit.MINUTES)
                connectTimeout(1, java.util.concurrent.TimeUnit.MINUTES)
                addInterceptor(BasicAuthInterceptor())
            }

            return clientBuilder.build()
        }

        fun getInstance(): RetrofitService {
            return retrofitService
        }
    }

}