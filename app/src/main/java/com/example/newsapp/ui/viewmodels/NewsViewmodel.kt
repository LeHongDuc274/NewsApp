package com.example.newsapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.newsapp.data.Repositories

class NewsViewmodel :ViewModel() {
    private val repositories = Repositories.getInstance()

    fun getTopHeadlinesNews(){
    }
    fun getSearchNews(){

    }

}