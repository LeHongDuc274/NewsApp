package com.example.newsapp.ui.viewmodels

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BrowserViewModel(val app:Application) : AndroidViewModel(app) {
    val currentUrl = MutableLiveData<String>("")
    val message = MutableLiveData<String>()
    fun copyLinkToClipBoard() {
        val copyLink  = currentUrl.value
        copyLink?.let { link ->
            val clipboard = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("simple text", link)
            clipboard.setPrimaryClip(clip)
            message.value = "Đã lưu vào bộ nhớ tạm"
        }
    }
}