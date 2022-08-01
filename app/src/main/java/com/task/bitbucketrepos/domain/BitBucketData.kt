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
    ) : Parcelable {
        companion object {
            fun mockRepo() = RepoData(
                "1234",
                "EN",
                Links.mockLinks(),
                Project.mockProduct()
            )
        }
    }

    @Parcelize
    data class Project(
        @SerializedName("name")
        val name: String
    ) : Parcelable {
        companion object {
            fun mockProduct() = Project("name")
        }
    }

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

        companion object {
            fun mockLinks() = Links(Avatar("imageUrl"))
        }
    }

    companion object {
        fun mock() = BitBucketData(
            repoList = listOf(
                RepoData.mockRepo()
            )
        )
    }
}