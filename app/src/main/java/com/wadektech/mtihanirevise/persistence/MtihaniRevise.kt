package com.wadektech.mtihanirevise.persistence

import android.app.Application
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import timber.log.Timber
import timber.log.Timber.DebugTree

class MtihaniRevise : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
        app = this
        initTheme()
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
        val selectedTheme = preferences.getString("night_mode_state_key", "")
        if (selectedTheme!=null){
            when(selectedTheme){
                "System Default" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                "Dark Mode" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                "Day Mode" ->  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}