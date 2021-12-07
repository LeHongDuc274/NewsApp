package com.example.newsapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.databinding.FragmentTopNewsBinding
import com.example.newsapp.ui.viewmodels.NewsViewmodel
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.newsapp.data.remote.models.Article
import com.example.newsapp.ui.BrowserActivity
import com.example.newsapp.ui.BrowserActivity.Companion.URL


class TopNewsFragment : Fragment() {
    private var _binding: FragmentTopNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewmodel: NewsViewmodel
    private lateinit var adapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTopNewsBinding.inflate(inflater, container, false)
        val view = binding.root
        viewmodel = ViewModelProvider(requireActivity())[NewsViewmodel::class.java]

        initRecyclerView()
        observerData()
//        viewmodel.getTopHeadlinesNews()
        return view
    }

    private fun observerData() {
        viewmodel.isNetWorkConnected.observe(viewLifecycleOwner) {
            if (it && viewmodel.listTopNews.value.isNullOrEmpty()) {
                viewmodel.getTopHeadlinesNews()
            }
        }
        viewmodel.listTopNews.observe(viewLifecycleOwner) {
            adapter.setData(it, viewmodel.isLastPageTopNews)
        }
        viewmodel.isLoading.observe(viewLifecycleOwner) {
            if (it == true) binding.progressCircular.visibility = View.VISIBLE
            else binding.progressCircular.visibility = View.GONE
        }
    }

    private fun initRecyclerView() {
        val dividerItemDecoration = DividerItemDecoration(
            requireActivity(),
            LinearLayoutManager.VERTICAL
        )
        adapter = NewsAdapter({
            openWebView(it)
        })
        adapter.setloadMoreClick {
            loadMore()
            Toast.makeText(
                requireActivity(),
                viewmodel.listTopNews.value?.size.toString() + " item",
                Toast.LENGTH_LONG
            ).show()
        }
        binding.rvTopNews.adapter = adapter
        binding.rvTopNews.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvTopNews.addItemDecoration(dividerItemDecoration)
    }

    private fun openWebView(article: Article) {
        viewmodel.isInApp.value = true
        if (viewmodel.isNetWorkConnected.value!!) {
            val intent = Intent(requireActivity(), BrowserActivity::class.java)
            intent.putExtra(URL, article.url)
            startActivity(intent)
        } else {
            viewmodel._message.value =
                requireActivity().getString(R.string.mess_internet_disconnected)
        }
    }

    private fun loadMore() {
        viewmodel.getTopHeadlinesNews()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}