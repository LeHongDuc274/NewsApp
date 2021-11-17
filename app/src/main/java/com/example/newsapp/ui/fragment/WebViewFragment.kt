package com.example.newsapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.newsapp.Contains.ARTICLE_ARGS_KEY
import com.example.newsapp.R
import com.example.newsapp.data.remote.models.Article
import com.example.newsapp.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {
    private var _binding : FragmentWebViewBinding ? = null
    private val binding get() =  _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebViewBinding.inflate(inflater,container,false)
        val view = binding.root
        val article = arguments?.getSerializable(ARTICLE_ARGS_KEY) as Article
        article.let {
            binding.webView.apply {
                webViewClient = WebViewClient()
                loadUrl(article.url)
            }
        }
        return view

    }

}