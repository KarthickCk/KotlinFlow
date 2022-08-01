package com.task.bitbucketrepos.data

import androidx.annotation.VisibleForTesting
import com.task.bitbucketrepos.domain.BitBucketApi
import com.task.bitbucketrepos.domain.BitBucketData
import com.task.bitbucketrepos.domain.IBitBucketRepository
import com.task.bitbucketrepos.domain.ICacheData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class BitBucketRepositoryImpl @Inject constructor(
    private val bitBucketApi: BitBucketApi,
    private val ioDispatcher: CoroutineDispatcher,
    private val iCacheData: ICacheData
) : IBitBucketRepository {

    override suspend fun getBitBucketList(): Flow<List<BitBucketData.RepoData>> {
        return merge(getCachedData(), getNetworkData())
            .flowOn(ioDispatcher)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNetworkData(): Flow<List<BitBucketData.RepoData>>  {
        return flow {
            emit(bitBucketApi.getRepoList().repoList)
        }.map {
            iCacheData.saveData(it)
            it
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getCachedData(): Flow<List<BitBucketData.RepoData>> {
        return flow {
            emit(iCacheData.getData())
        }
    }
}