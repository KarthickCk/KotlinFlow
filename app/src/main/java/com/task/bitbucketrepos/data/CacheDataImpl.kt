package com.task.bitbucketrepos.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.task.bitbucketrepos.domain.BitBucketData
import com.task.bitbucketrepos.domain.ICacheData

class CacheDataImpl(
    private val gson: Gson,
    private val sharedPreferences: SharedPreferences
) : ICacheData {
    override fun saveData(list: List<BitBucketData.RepoData>) {
        val dataString = gson.toJson(list).toString()
        sharedPreferences.edit().apply {
            putString(REPO_DATA, dataString)
            apply()
        }
    }

    override fun getData(): List<BitBucketData.RepoData> {
        val dataString = sharedPreferences.getString(REPO_DATA, null) ?: return emptyList()
        val myType = object : TypeToken<List<BitBucketData.RepoData>>() {}.type
        return gson.fromJson(dataString, myType)
    }

    companion object {
        const val REPO_DATA = "repo_data"
    }
}