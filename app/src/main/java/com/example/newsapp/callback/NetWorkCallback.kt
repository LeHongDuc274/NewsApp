package com.example.newsapp.callback
interface NetWorkCallback<T> {
    fun onSucces(data : List<T>?)
    fun onFailure(message: String)

}