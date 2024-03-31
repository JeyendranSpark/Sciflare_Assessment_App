package com.jeyendroid.sciflare_assessment_app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {

    companion object {
        private var sInstance: MyApplication? = null

        val instance: MyApplication
            @Throws(RuntimeException::class)
            get() {
                if (sInstance == null) throw RuntimeException((sInstance as MyApplication).getString(R.string.no_instance_found))
                return sInstance as MyApplication
            }
    }

    override fun onCreate() {
        super.onCreate()
        if (sInstance == null) {
            sInstance = this
        }
    }


}