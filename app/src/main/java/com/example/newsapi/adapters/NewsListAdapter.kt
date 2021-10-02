package com.example.newsapi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.databinding.ItemArticleBinding
import com.example.newsapi.models.Article
import com.example.newsapi.ui.fragments.BreakingNewsDirections
import com.example.newsapi.utils.loadImage

class NewsListAdapter: RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsViewHolder(
        ItemArticleBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
    )

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentArticle = differ.currentList[position]
        val currentView = holder.binding

        currentView.tvTitle.text = currentArticle.title
        currentView.tvPublishedAt.text = currentArticle.publishedAt
        currentView.tvDescription.text = currentArticle.description
        currentView.tvSource.text = currentArticle.source.name
        currentView.ivArticleImage.loadImage(currentArticle.urlToImage)

        currentView.root.setOnClickListener {
            onItemClickListener?.let { it(currentArticle) }
        }
    }

    override fun getItemCount() = differ.currentList.size

    class NewsViewHolder
    constructor(val binding: ItemArticleBinding):
    RecyclerView.ViewHolder(binding.root)

    //Item Click Listener
    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    //Differ Callback
    private val differCallback = object: DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}