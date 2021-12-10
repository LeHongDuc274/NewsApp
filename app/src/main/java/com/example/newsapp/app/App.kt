package com.example.newsapp.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.newsapp.Contains
import com.example.newsapp.Contains.IS_BACKGROUND
import com.example.newsapp.Contains.KEY_GET_BOOL

class App : Application(), DefaultLifecycleObserver {

    override fun onCreate() {
        super<Application>.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStop(owner: LifecycleOwner) {
        val sharedPrefs =
            this@App.getSharedPreferences(IS_BACKGROUND, Context.MODE_PRIVATE) ?: null
        val bool = sharedPrefs?.edit()
        bool?.putBoolean(KEY_GET_BOOL, true)
        bool?.apply()
    }
}


