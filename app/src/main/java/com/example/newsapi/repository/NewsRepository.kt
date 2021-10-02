package com.example.newsapi.repository

import androidx.lifecycle.LiveData
import com.example.newsapi.api.RetroInstance
import com.example.newsapi.database.ArticleDatabase
import com.example.newsapi.models.Article

class NewsRepository
constructor(private val database: ArticleDatabase) {

    suspend fun insertArticle(article: Article) = database.getArticleDao().insertArticle(article)

    fun getAllArticles(): LiveData<List<Article>> = database.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = database.getArticleDao().deleteArticle(article)
}