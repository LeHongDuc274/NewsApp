package com.example.newsapp.app

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.newsapp.Contains
import com.example.newsapp.Contains.IS_BACKGROUND
import com.example.newsapp.Contains.KEY_GET_BOOL
import com.example.newsapp.ui.LoginActivity
import com.example.newsapp.ui.MainActivity

class App : Application(), ActivityLifecycleCallbacks, DefaultLifecycleObserver {
    private var isBecameFromBackGround = false
    override fun onCreate() {
        super<Application>.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        registerActivityLifecycleCallbacks(this)
    }

    override fun onStop(owner: LifecycleOwner) {
        isBecameFromBackGround = true
    }


    override fun onActivityResumed(activity: Activity) {
        if (isBecameFromBackGround) {
            isBecameFromBackGround = false
            if (!(activity is LoginActivity)) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra(IS_BACKGROUND, true)
                activity.startActivity(intent)
            }
        }
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) = Unit
    override fun onActivityStarted(p0: Activity) = Unit
    override fun onActivityPaused(p0: Activity) = Unit
    override fun onActivityStopped(p0: Activity) = Unit
    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) = Unit
    override fun onActivityDestroyed(p0: Activity) = Unit
}


