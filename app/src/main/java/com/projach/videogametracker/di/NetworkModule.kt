package com.projach.videogametracker.di

import com.projach.videogametracker.BuildConfig
import com.projach.videogametracker.data.source.remote.IgdbApi
import com.projach.videogametracker.data.source.remote.IgdbTokenManager
import com.projach.videogametracker.data.source.remote.TwitchApi
import com.projach.videogametracker.domain.IGDB_BASE_URL
import com.projach.videogametracker.domain.TWIRCH_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IgdbRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TwitchRetrofit

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenManager: IgdbTokenManager): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor { chain ->
            val token = runBlocking { tokenManager.getValidToken() }
            val request = chain.request()
                .newBuilder()
                .addHeader("Client-ID", BuildConfig.IGDB_CLIENT_ID)
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClient.addInterceptor(logging)
        }

        return okHttpClient.build()
    }

    @Provides
    @Singleton
    @IgdbRetrofit
    fun provideIgdbRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(IGDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideIgdbEndpoints(@IgdbRetrofit retrofit: Retrofit): IgdbApi =
        retrofit.create(IgdbApi::class.java)

    @Provides
    @Singleton
    @TwitchRetrofit
    fun provideTwitchRetrofit(): Retrofit =
        Retrofit.Builder()
            .client(OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    this.addInterceptor(logging)
                }
            }.build())
            .baseUrl(TWIRCH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideTwitchEndpoints(@TwitchRetrofit retrofit: Retrofit): TwitchApi =
        retrofit.create(TwitchApi::class.java)
}