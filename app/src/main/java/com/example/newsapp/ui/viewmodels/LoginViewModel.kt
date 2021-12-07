package com.example.newsapp.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.Contains.KEY_GET_PASS
import com.example.newsapp.Contains.KEY_PREFS
import com.example.newsapp.R

class LoginViewModel(val app: Application) : AndroidViewModel(app) {

    var oldPass = MutableLiveData<String?>(null)
    var message = MutableLiveData<String>("")
    init {
        oldPass.value = getRefs()
    }

    fun checkPassCode(text: String): Boolean {
        if (text.isNotEmpty() && !oldPass.value.isNullOrEmpty() && text == oldPass.value) {
            return true
        } else {
            message.value = app.getString(R.string.mess_pass_wrong)
            return false
        }
    }

    fun getRefs(): String? {
        val sharedPrefs = app.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE) ?: null
        val defaultValue: String? = null
        val pass = sharedPrefs?.getString(KEY_GET_PASS, defaultValue)
        return pass
    }

    fun saveRefs(text: String) :Boolean {
        if (text.isEmpty()) {
            message.value = app.getString(R.string.mess_pass_missing)
            return false
        }
        val sharedPrefs = app.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE) ?: null
        val pass = sharedPrefs?.edit()
        pass?.putString(KEY_GET_PASS, text)
        pass?.apply()
        message.value = app.getString(R.string.mess_pass_save_success)
        return true
    }
}