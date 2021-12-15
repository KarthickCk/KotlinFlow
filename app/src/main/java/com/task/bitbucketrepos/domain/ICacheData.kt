package com.task.bitbucketrepos.domain

interface ICacheData {
    fun saveData(list: List<BitBucketData.RepoData>)
    fun getData(): List<BitBucketData.RepoData>
}