package com.example.newsapp.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.newsapp.Contains

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onStart(owner: LifecycleOwner) { // app moved to foreground
            }

            override fun onStop(owner: LifecycleOwner) { // app moved to background
                val sharedPrefs =
                    this@App.getSharedPreferences("isBackBround", Context.MODE_PRIVATE) ?: null
                val bool = sharedPrefs?.edit()
                bool?.putBoolean("key_background", true)
                bool?.apply()
            }
        })
    }
}

