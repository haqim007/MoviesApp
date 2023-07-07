package dev.haqim.moviesapp.data.remote.network

import dev.haqim.moviesapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiConfig @Inject constructor() {

    private fun createRetrofit(
        httpClient: OkHttpClient.Builder,
        baseUrl: String = BuildConfig.BASE_URL
    ): Retrofit {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        httpClient
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}").build()
                chain.proceed(request)
            }

        if(BuildConfig.DEBUG){
            httpClient
                .addInterceptor(loggingInterceptor)
        }

        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    fun <ServiceClass> createService(
        serviceClass: Class<ServiceClass>
    ): ServiceClass {
        val httpClient = OkHttpClient.Builder()
        val retrofit = createRetrofit(httpClient)
        return retrofit.create(serviceClass)
    }

}