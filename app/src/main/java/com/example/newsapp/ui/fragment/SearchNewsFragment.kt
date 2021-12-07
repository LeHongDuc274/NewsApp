package com.example.newsapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.Contains
import com.example.newsapp.Contains.DELAY_TIME_SEARCH
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.data.remote.models.Article
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.ui.BrowserActivity
import com.example.newsapp.ui.viewmodels.NewsViewmodel
import kotlinx.coroutines.*


class SearchNewsFragment : Fragment() {
    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewmodel: NewsViewmodel
    private lateinit var adapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        val view = binding.root
        viewmodel = ViewModelProvider(requireActivity())[NewsViewmodel::class.java]
        initRecyclerView()
        observerData()
        setupSearchView()
        return view
    }

    private fun setupSearchView() {
        var job: Job? = null
        binding.tvSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = CoroutineScope(Dispatchers.Default).launch {
                delay(DELAY_TIME_SEARCH)
                if (editable != null && editable.toString().isNotEmpty()) {
                    viewmodel.resetNewQuery(editable.toString())
                    //  delay(DELAY_TIME_SEARCH/2)
                    viewmodel.getSearchNews()
                }
            }
        }
    }

    private fun openWebView(article: Article) {
        if (viewmodel.isNetWorkConnected.value!!) {
            val intent = Intent(requireActivity(), BrowserActivity::class.java)
            intent.putExtra(BrowserActivity.URL, article.url)
            startActivity(intent)
        } else {
            viewmodel._message.value =
                requireActivity().getString(R.string.mess_internet_disconnected)
        }
    }

    private fun observerData() {
        viewmodel.listSearchNews.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
        viewmodel.isLoading.observe(viewLifecycleOwner) {
            if (it == true) binding.progressCircular.visibility = View.VISIBLE
            else binding.progressCircular.visibility = View.GONE
        }
    }

    private fun initRecyclerView() {
        adapter = NewsAdapter({
            openWebView(it)
        })
        adapter.setloadMoreClick {
            loadMore()
        }
        binding.rvSearchNews.adapter = adapter
        binding.rvSearchNews.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }

    private fun loadMore() {
        viewmodel.getSearchNews()
    }

}