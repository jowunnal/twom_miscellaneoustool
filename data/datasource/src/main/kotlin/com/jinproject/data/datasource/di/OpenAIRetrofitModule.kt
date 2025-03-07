package com.jinproject.data.datasource.di

import com.google.gson.GsonBuilder
import com.jinproject.data.datasource.BuildConfig
import com.jinproject.data.datasource.remote.api.GenerateImageApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.io.IOException
import java.time.Duration
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OpenAIRetrofitModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OpenAIRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OpenAIOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FileDownloadOkHttpClient

    @Singleton
    @Provides
    @FileDownloadOkHttpClient
    fun provideFileDownloadOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .readTimeout(Duration.ofSeconds(60))
            .writeTimeout(Duration.ofSeconds(30))
            .build()
    }

    @OpenAIOkHttpClient
    @Singleton
    @Provides
    fun provideOpenAIOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(HeaderInterceptor("Bearer ${BuildConfig.OPENAI_APIKEY}"))
            .readTimeout(Duration.ofSeconds(60))
            .writeTimeout(Duration.ofSeconds(30))
            .build()
    }

    @OpenAIRetrofit
    @Singleton
    @Provides
    fun provideOpenAIRetrofit(@OpenAIOkHttpClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl("https://api.openai.com/v1/")
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun createGeneratingImageAPI(@OpenAIRetrofit retrofit: Retrofit): GenerateImageApi =
        retrofit.create()
}

internal class HeaderInterceptor @Inject constructor(private val accessToken: String) :
    Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", accessToken)
            .build()

        return chain.proceed(newRequest)
    }
}