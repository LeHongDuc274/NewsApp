package com.example.newsapp.ui.viewmodels

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.Contains
import com.example.newsapp.Contains.PAGE_SIZE
import com.example.newsapp.R
import com.example.newsapp.callback.NetWorkCallback
import com.example.newsapp.data.Repositories
import com.example.newsapp.data.remote.models.Article

class NewsViewmodel(val app: Application) : AndroidViewModel(app) {

    var isBackGround = MutableLiveData(false)
    val isInApp = MutableLiveData(false)
    var isNetWorkConnected = MutableLiveData<Boolean>(false)
    private val repositories = Repositories.getInstance()
    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading
    var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private var _listTopNews: MutableLiveData<MutableList<Article>> = MutableLiveData()
    val listTopNews: LiveData<MutableList<Article>> = _listTopNews
    private var _listSearchNews = MutableLiveData<MutableList<Article>>()
    val listSearchNews: LiveData<MutableList<Article>> = _listSearchNews

    var curTopNewsPage = 1
    var curSearchNewPage = 1
    var isLastPageTopNews = false
    var isLastPageSearchNews = false
    var newQuery: String = ""
    val broadcastInternet = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            p1?.let {
                if (it.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                    val state = Contains.checkNetWorkAvailable(app)
                    isNetWorkConnected.value = state
                    if (!state) _message.value = "No internet"
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
        if (!state) _message.value = app.getString(
            R.string.mess_internet_disconnected
        )
    }

    fun getTopHeadlinesNews() {
        if (!isLastPageTopNews && isNetWorkConnected.value!!) {
            _isLoading.value = true
            repositories.getTopHeadlinesNews(object : NetWorkCallback<Article> {
                override fun onSucces(data: List<Article>?, totalResults: Int?) {
                    totalResults?.let {
                        isLastPageTopNews = ((it / PAGE_SIZE) + 2 - curTopNewsPage) < 0
                    }
                    data?.let {
                        val newList = it.toMutableList()
                        val oldList = _listTopNews.value ?: mutableListOf()
                        oldList.addAll(newList)
                        _listTopNews.value = oldList
                        _isLoading.value = false
                    }
                }

                override fun onFailure(message: String?) {
                    _message.value = message ?: app.getString(R.string.mess_error)
                    _isLoading.value = false
                }
            }, curTopNewsPage)
            curTopNewsPage++
        }
    }

    fun getSearchNews() {
        if (!isLastPageSearchNews && isNetWorkConnected.value!!) {
            _isLoading.postValue(true)
            repositories.getSearchNews(newQuery, object : NetWorkCallback<Article> {
                override fun onSucces(data: List<Article>?, totalResults: Int?) {
                    totalResults?.let {
                        isLastPageSearchNews = ((it / PAGE_SIZE) + 2 - curSearchNewPage) < 0
                    }
                    data?.let {
                        val newList = it.toMutableList()
                        val oldList = _listSearchNews.value ?: mutableListOf()
                        oldList.addAll(newList)
                        _listSearchNews.value = oldList
                    }
                }

                override fun onFailure(message: String?) {
                    _message.value = message ?: app.getString(R.string.mess_error)
                }
            }, curSearchNewPage)
            curSearchNewPage++
            _isLoading.postValue(false)
        }
    }

    fun resetNewQuery(query: String) {
        newQuery = query
        _listSearchNews.postValue(mutableListOf())
        curSearchNewPage = 1
        isLastPageSearchNews = false
    }
}