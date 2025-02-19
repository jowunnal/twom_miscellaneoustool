package com.jinproject.data.di

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.google.gson.GsonBuilder
import com.jinproject.data.BuildConfig
import com.jinproject.data.datasource.cache.CollectionDataStorePreferences
import com.jinproject.data.datasource.remote.api.GenerateImageApi
import com.jinproject.data.di.OpenAIRetrofitModule.FileDownloadOkHttpClient
import com.jinproject.domain.model.HttpException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.time.Duration
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.coroutines.resumeWithException

@Module
@InstallIn(SingletonComponent::class)
internal object OpenAIRetrofitModule {

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