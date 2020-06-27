package com.wadektech.mtihanirevise.persistence

import android.app.Application
import android.preference.PreferenceManager
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import com.wadektech.mtihanirevise.utils.ThemeManager
import timber.log.Timber
import timber.log.Timber.DebugTree

class MtihaniRevise : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
        app = this
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        val builder = Picasso.Builder(this)
        builder.downloader(OkHttpDownloader(applicationContext))
        val built = builder.build()
        built.setIndicatorsEnabled(true)
        built.isLoggingEnabled = true
        Picasso.setSingletonInstance(built)
    }

    companion object {
        var app: MtihaniRevise? = null
            private set
    }

    private fun initTheme() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        ThemeManager.applyTheme(preferences.getString(true.toString(), "")!!)
    }
}