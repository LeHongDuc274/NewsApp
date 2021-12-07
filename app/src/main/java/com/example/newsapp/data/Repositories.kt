package com.example.newsapp.data

import com.example.newsapp.callback.NetWorkCallback
import com.example.newsapp.data.remote.api.RetrofitInstance
import com.example.newsapp.data.remote.models.Article
import com.example.newsapp.data.remote.models.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repositories {
    companion object {
        private var instance: Repositories? = null
        fun getInstance(): Repositories {
            if (instance == null) {
                synchronized(this) {
                    instance = Repositories()
                }
            }
            return instance!!
        }
    }

    fun getTopHeadlinesNews(netWorkCallback: NetWorkCallback<Article>, page: Int) {
        RetrofitInstance.newsApi.getTopHeadlines(page = page)
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    response.body()?.let {
                        if (response.isSuccessful) {
                            netWorkCallback.onSucces(it.articles, it.totalResults)
//                      if(it.status == "error"){
//                          netWorkCallback.onFailure("ererererer")
//                      }
                        }
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    netWorkCallback.onFailure(t.message.toString())
                }
            })
    }

    fun getSearchNews(query: String, netWorkCallback: NetWorkCallback<Article>, page: Int) {
        RetrofitInstance.newsApi.getSearchNews(query = query, page = page)
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    response.body()?.let {
                        if (response.isSuccessful) {
                            netWorkCallback.onSucces(it.articles, it.totalResults)
//                        if(it.status == "error"){
//                            netWorkCallback.onFailure("errrrrrrrrrorro")
//                        }
                        }
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    netWorkCallback.onFailure(t.message.toString())
                }
            })
    }
}