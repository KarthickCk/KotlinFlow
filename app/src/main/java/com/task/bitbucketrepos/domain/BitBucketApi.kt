package com.task.bitbucketrepos.domain

import retrofit2.http.GET

interface BitBucketApi {
    @GET("repositories")
    suspend fun getRepoList(): BitBucketData
}