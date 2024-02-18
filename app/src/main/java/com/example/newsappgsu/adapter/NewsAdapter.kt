package com.example.newsappgsu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.newsapp.model.ArticlesItem
import com.example.newsappgsu.databinding.ItemNewsBinding

class NewsAdapter(var articlesItem: List<ArticlesItem?>?):Adapter<NewsAdapter.NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        var itemBinding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NewsHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return articlesItem?.size ?: 0
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val articleItem = articlesItem?.get(position)
        holder.bind(articleItem)
    }

    fun changeData(articles: List<ArticlesItem?>?) {
        this.articlesItem = articles
        notifyDataSetChanged()
    }

    class NewsHolder(private val binding: ItemNewsBinding):ViewHolder(binding.root){
        fun bind(articlesItem: ArticlesItem?){
            binding.date.setText(articlesItem?.publishedAt)
            binding.title.setText(articlesItem?.title)
            binding.desc.setText(articlesItem?.description)
            Glide.with(binding.root)
                .load(articlesItem?.urlToImage)
                .into(binding.image)

        }
    }
}