package com.task.bitbucketrepos.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.task.bitbucketrepos.data.BitBucketRepositoryImpl
import com.task.bitbucketrepos.data.CacheDataImpl
import com.task.bitbucketrepos.domain.BitBucketApi
import com.task.bitbucketrepos.domain.IBitBucketRepository
import com.task.bitbucketrepos.domain.ICacheData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class HiltRepoModule {

    @Provides
    @Named(IO_DISPATCHER)
    fun providesIODispatcher() = Dispatchers.IO

    @Provides
    fun providesSharedPreferences(
        @ApplicationContext context: Context
    ) = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    fun providesGson() = Gson()

    @Provides
    fun providesCacheData(gson: Gson, sharedPreferences: SharedPreferences): ICacheData {
        return CacheDataImpl(gson, sharedPreferences)
    }

    @Provides
    fun providesBitBucketRepository(
        bitBucketApi: BitBucketApi,
        @Named(IO_DISPATCHER) ioDispatcher: CoroutineDispatcher,
        iCacheData: ICacheData
    ): IBitBucketRepository {
        return BitBucketRepositoryImpl(bitBucketApi, ioDispatcher, iCacheData)
    }

    companion object {
        private const val IO_DISPATCHER = "IO_DISPATCHER"
        private const val APP_PREFERENCES = "app_preferences"
    }
}