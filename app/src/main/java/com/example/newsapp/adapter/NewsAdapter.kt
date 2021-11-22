package com.example.newsapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.Contains.VIEWTYPE_LOADMORE
import com.example.newsapp.Contains.VIEWTYPE_NORMAL
import com.example.newsapp.Contains.convertDatetime
import com.example.newsapp.R
import com.example.newsapp.data.remote.models.Article
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(
    var itemClick: ((Article) -> Unit)? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listNews = mutableListOf<Article?>()
    private var loadMore_click: (() -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivArticle = itemView.findViewById<ImageView>(R.id.iv_article)
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        val tvDescription = itemView.findViewById<TextView>(R.id.tv_description)
        val tvSource = itemView.findViewById<TextView>(R.id.tv_source)
        val tvDateTime = itemView.findViewById<TextView>(R.id.tv_datetime)
        fun onBind(position: Int) {
            val articl = listNews[position]
            articl?.let { article ->
                tvTitle.text = article.title
                tvDescription.text = article.description
                tvSource.text = article.source.name
                tvDateTime.text = convertDatetime(article.publishedAt)
                Glide.with(itemView).load(article.urlToImage)
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .into(ivArticle)

                itemView.setOnClickListener {
                    itemClick?.invoke(article)
                }
            }
        }
    }

    inner class ViewHolderLoadmore(item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEWTYPE_NORMAL -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_new, parent, false)
            )
            else -> ViewHolderLoadmore(
                LayoutInflater.from(parent.context).inflate(R.layout.item_load_more, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEWTYPE_NORMAL) {
            (holder as ViewHolder).onBind(position)
        } else if (holder.itemViewType == VIEWTYPE_LOADMORE) {
            (holder as ViewHolderLoadmore).itemView.findViewById<Button>(R.id.btn_loadmore)
                .setOnClickListener {
                    loadMore_click?.invoke()
                }
        }
    }

    override fun getItemCount(): Int = listNews.size

    override fun getItemViewType(position: Int): Int {
        return if (listNews[position] == null) VIEWTYPE_LOADMORE else VIEWTYPE_NORMAL
    }

    fun setData(listNew: List<Article>, isLastpage: Boolean = false) {
        listNews = listNew.toMutableList()
        if (!isLastpage && listNews.isNotEmpty()) listNews.add(null)
        notifyDataSetChanged()
    }

    fun setloadMoreClick(action: (() -> Unit)? = null) {
        loadMore_click = action
    }
}