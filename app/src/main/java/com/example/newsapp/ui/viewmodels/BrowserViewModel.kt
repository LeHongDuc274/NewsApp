package com.example.newsapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BrowserViewModel : ViewModel() {
    var currentUrl = MutableLiveData<String>("")
    var isBackGround = MutableLiveData(false)
}