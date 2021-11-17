package com.example.newsapp.ui.viewmodels

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.Contains
import com.example.newsapp.callback.NetWorkCallback
import com.example.newsapp.data.Repositories
import com.example.newsapp.data.remote.models.Article

class NewsViewmodel(val app : Application) : AndroidViewModel(app) {

    var isNetWorkConnected = MutableLiveData<Boolean>()
    private val repositories = Repositories.getInstance()
    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading
    var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private var _listTopNews = MutableLiveData<List<Article>>()
    val listTopNews: LiveData<List<Article>> = _listTopNews
    private var _listSearchNews = MutableLiveData<List<Article>>()
    val listSearchNews: LiveData<List<Article>> = _listSearchNews


    val broadcastInternet = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            p1?.let {
                if (it.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                    val state = Contains.checkNetWorkAvailable(app)
                    isNetWorkConnected.value = state
                   _message.value = if (state) "Internet Connected" else "No Internet"
                }
            }
        }
    }
    init {
        checkNetWork()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        app.registerReceiver(broadcastInternet, filter)
    }
    override fun onCleared() {
        super.onCleared()
        app.unregisterReceiver(broadcastInternet)
    }
    private fun checkNetWork() {
        val state = Contains.checkNetWorkAvailable(app)
        isNetWorkConnected.value = state
        if (!state) _message.value = "No Internet"
    }

    fun getTopHeadlinesNews() {
        _isLoading.value = true
        repositories.getTopHeadlinesNews(object : NetWorkCallback<Article> {
            override fun onSucces(data: List<Article>?) {
                data?.let {
                    _listTopNews.value = it
                    _isLoading.value = false
                }
            }

            override fun onFailure(message: String?) {
                _message.value = message ?: "error"
                _isLoading.value = false
            }
        })

    }

    fun getSearchNews(query: String) {
        isNetWorkConnected.value?.let {
            _isLoading.postValue(true)
            repositories.getSearchNews(query, object : NetWorkCallback<Article> {
                override fun onSucces(data: List<Article>?) {
                    data?.let {
                        _listSearchNews.value = it
                    }
                }
                override fun onFailure(message: String?) {
                    _message.value = message ?: "error"
                }
            })
            _isLoading.postValue(false)
        }
    }
}