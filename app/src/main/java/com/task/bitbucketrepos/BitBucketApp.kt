package com.task.bitbucketrepos

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BitBucketApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}