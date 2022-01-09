package com.example.newsapp.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

import com.example.newsapp.databinding.ActivityBrowserBinding
import com.example.newsapp.ui.viewmodels.BrowserViewModel

import android.widget.Toolbar
import com.example.newsapp.R


class BrowserActivity : AppCompatActivity() {
    companion object {
        const val URL = "pageUrl"
        const val MAX_PROGRESS = 100
    }

    private var _binding: ActivityBrowserBinding? = null
    private val binding get() = _binding!!
    private lateinit var url: String
    private lateinit var viewModel: BrowserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[BrowserViewModel::class.java]
        _binding = ActivityBrowserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        url = intent.getStringExtra(URL) ?: "www.google.com.vn"
        binding.progressBar.max = MAX_PROGRESS
        initWebView()
        loadUrl()
        initControlToolbar()
        obverseMessage()

    }

    private fun obverseMessage() {
        viewModel.message.observe(this) { mess ->
            Toast.makeText(this, mess, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initControlToolbar() {
        val toolbar = binding.toolBarWebview
        setSupportActionBar(toolbar)

        binding.ivBack.setOnClickListener {
            finish()
        }
        viewModel.currentUrl.observe(this) {
            binding.tvUrl.text = it
        }
    }

    private fun loadUrl() {
        binding.webView.loadUrl(url)
        val snapshotList = binding.webView.copyBackForwardList()
        viewModel.currentUrl.value = snapshotList.currentItem?.url ?: url
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.let {
                    viewModel.currentUrl.value = it.url.toString()
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.progress = MAX_PROGRESS
                binding.progressBar.visibility = View.GONE
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = View.VISIBLE
                binding.progressBar.progress = 0
            }
        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                binding.progressBar.progress = newProgress
            }
        }
    }

    // back buttom envent
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
            binding.webView.goBack()
            val snapshotList = binding.webView.copyBackForwardList()
            viewModel.currentUrl.value = snapshotList.currentItem?.url ?: url
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share -> {
                shareCurrentLink()
                true
            }
            R.id.copy_link -> {
                copyLinkToClipBoard()
                true
            }
            R.id.open_other_app -> {
                openLinkOnBrowser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareCurrentLink() {
        viewModel.currentUrl.value?.let { link ->
            val shareIntent = Intent.createChooser(Intent().apply {
                action = ACTION_SEND
                putExtra(EXTRA_TEXT,link)
                putExtra(EXTRA_TITLE,"News")
                type = "text/plain"
            },null)
            startActivity(shareIntent)
        }
    }

    private fun openLinkOnBrowser() {
        viewModel.currentUrl.value?.let { link ->
            val intent = Intent(ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }
    }

    private fun copyLinkToClipBoard() {
        viewModel.copyLinkToClipBoard()
    }
}