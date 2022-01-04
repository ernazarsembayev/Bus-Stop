package com.yernazar.pidapplication.data.repository.server.api

import com.yernazar.pidapplication.utils.config.Config.CONNECT_TIMEOUT
import com.yernazar.pidapplication.utils.config.Config.READ_TIMEOUT
import com.yernazar.pidapplication.utils.config.Config.WRITE_TIMEOUT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class BaseApi {

    companion object {

        fun createRetrofit(baseUrl: String, token: String?): Retrofit {

            val okHttpClientBuilder = OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(Interceptor {
                    return@Interceptor onIntercept(it, token)
                })

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private fun onIntercept(chain: Interceptor.Chain, token: String?): okhttp3.Response {
            try {

                val response = chain.proceed(chain.request())
                response.body()?.let { responseBody ->
                    val content = responseBody.string()
                    val responseX = response.newBuilder()

                    token?.let { token ->
                        responseX.addHeader("Authorization", "Bearer " + token)
                    }

                    return responseX
                        .body(ResponseBody.create(responseBody.contentType(), content))
                        .build()
                }

            } catch (exception: SocketTimeoutException) {
                exception.printStackTrace()
            }

            return chain.proceed(chain.request())
        }

    }
}