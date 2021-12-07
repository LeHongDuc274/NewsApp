package com.example.newsapp.callback
interface NetWorkCallback<T> {
    fun onSucces(data : List<T>?,totalResults:Int?)
    fun onFailure(message: String?)
}