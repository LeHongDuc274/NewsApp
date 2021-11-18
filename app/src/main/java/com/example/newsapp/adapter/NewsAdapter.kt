package com.example.newsapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.Contains.convertDatetime
import com.example.newsapp.R
import com.example.newsapp.data.remote.models.Article
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(var itemClick: ((Article) -> Unit)? = null) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    private var listNews = mutableListOf<Article>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivArticle = itemView.findViewById<ImageView>(R.id.iv_article)
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        val tvDescription = itemView.findViewById<TextView>(R.id.tv_description)
        val tvSource = itemView.findViewById<TextView>(R.id.tv_source)
        val tvDateTime = itemView.findViewById<TextView>(R.id.tv_datetime)
        fun onBind(position: Int) {
            val article = listNews[position]

            tvTitle.text = article.title
            tvDescription.text = article.description
            tvSource.text = article.source.name
            tvDateTime.text = convertDatetime(article.publishedAt)
            Glide.with(itemView).load(article.urlToImage).into(ivArticle)

            itemView.setOnClickListener {
                itemClick?.invoke(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_new, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = listNews.size

    fun setData(listNew: List<Article>) {
        listNews = listNew.toMutableList()
        notifyDataSetChanged()
    }

}