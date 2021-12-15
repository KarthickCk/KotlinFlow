package com.task.bitbucketrepos.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BitBucketData(
    @SerializedName("values")
    val repoList: List<RepoData>
) : Parcelable {

    @Parcelize
    data class RepoData(
        @SerializedName("uuid")
        val uuid: String,
        @SerializedName("language")
        val language: String,
        @SerializedName("links")
        val links: Links,
        @SerializedName("project")
        val project: Project
    ) : Parcelable

    @Parcelize
    data class Project(
        @SerializedName("name")
        val name: String
    ) : Parcelable

    @Parcelize
    data class Links(
        @SerializedName("avatar")
        val avatar: Avatar
    ) : Parcelable {
        @Parcelize
        data class Avatar(
            @SerializedName("href")
            val imageUrl: String
        ) : Parcelable
    }
}