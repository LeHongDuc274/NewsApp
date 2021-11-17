package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.databinding.FragmentTopNewsBinding
import com.example.newsapp.ui.viewmodels.NewsViewmodel
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.newsapp.Contains.ARTICLE_ARGS_KEY
import com.example.newsapp.data.remote.models.Article


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
        viewmodel.isNetWorkConnected.observe(viewLifecycleOwner){
            Log.e("tag", viewmodel.listTopNews.value.isNullOrEmpty().toString() +"$it")
            if(it && viewmodel.listTopNews.value.isNullOrEmpty()){
                viewmodel.getTopHeadlinesNews()
            }
        }
        viewmodel.listTopNews.observe(viewLifecycleOwner) {
            adapter.setData(it)
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
        adapter = NewsAdapter{
            openWebView(it)
        }
        binding.rvTopNews.adapter = adapter
        binding.rvTopNews.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvTopNews.addItemDecoration(dividerItemDecoration)
    }
    private fun openWebView(article :Article){
        val bundle = bundleOf(ARTICLE_ARGS_KEY to article)
        findNavController().navigate(R.id.action_topNewsFragment_to_webViewFragment,bundle)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}