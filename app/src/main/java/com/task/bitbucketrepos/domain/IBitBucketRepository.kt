package com.task.bitbucketrepos.domain

import kotlinx.coroutines.flow.Flow

interface IBitBucketRepository {
    suspend fun getBitBucketList(): Flow<List<BitBucketData.RepoData>>
}