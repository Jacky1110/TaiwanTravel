package com.jotangi.nickyen

import android.app.Application
import android.content.res.Configuration
import android.content.res.Resources
import timber.log.Timber

/**
 *Created by Luke Liu on 2021/9/11.
 */

class DDotApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}