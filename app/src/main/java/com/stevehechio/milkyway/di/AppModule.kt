package com.stevehechio.milkyway.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.stevehechio.milkyway.db.AppDatabase
import com.stevehechio.milkyway.data.remote.api.MilkyWayApiService
import com.stevehechio.milkyway.data.repository.MilkyWayRepository
import com.stevehechio.milkyway.ui.viewmodel.MilkyWayViewModel
import com.stevehechio.milkyway.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideCache(application: Application): Cache {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val httpCacheDirectory = File(application.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize)
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.cache(cache)
        httpClient.addInterceptor(logging)
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        return httpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .baseUrl(AppConstants.BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideMilkyService(retrofit: Retrofit): MilkyWayApiService{
       return retrofit.create(MilkyWayApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.invoke(context)
    }

    @Provides
    @Singleton
    fun provideMilkyRepository(@ApplicationContext context: Context,service: MilkyWayApiService,
                               appDatabase: AppDatabase
    ): MilkyWayRepository{
        return MilkyWayRepository(context,service, appDatabase)
    }

    @Provides
    @Singleton
    fun provideMilkyViewModel(repository: MilkyWayRepository): MilkyWayViewModel{
        return MilkyWayViewModel(repository)
    }
}