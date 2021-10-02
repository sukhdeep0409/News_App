package com.example.newsapi.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.newsapi.api.RetroInstance
import com.example.newsapi.database.ArticleDatabase
import com.example.newsapi.models.Article
import com.example.newsapi.models.NewsResponse
import com.example.newsapi.repository.NewsRepository
import kotlinx.coroutines.*

class NewsViewModel
constructor(application: Application):
AndroidViewModel(application) {
    private val newsInstance = RetroInstance.API
    private var job: Job? = null
    private val repository: NewsRepository

    init {
        val db = ArticleDatabase.invoke(application)
        repository = NewsRepository(db)
    }

    // Room
    fun saveArticle(article: Article) =
        viewModelScope.launch(Dispatchers.IO) { repository.insertArticle(article) }

    fun getSavedArticles(): LiveData<List<Article>> = repository.getAllArticles()

    fun deleteArticle(article: Article) =
        viewModelScope.launch(Dispatchers.IO) { repository.deleteArticle(article) }


    // Retrofit
    val breakingNews: MutableLiveData<List<Article>> = MutableLiveData()
    val searchedNews: MutableLiveData<List<Article>> = MutableLiveData()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception Handled: ${throwable.localizedMessage}")
    }

    fun refresh() {
        fetchUsers()
    }

    fun searchNews(query: String) {
        newsSearch(query)
    }

    private fun fetchUsers() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = newsInstance.getBreakingNews("in")
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    breakingNews.value = response.body()?.articles
                }
                else {
                    onError("Error: ${response.body()}")
                }
            }
        }
    }

    private fun newsSearch(query: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = newsInstance.searchNews(query)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    searchedNews.value = response.body()?.articles
                }
            }
        }
    }

    private fun onError(message: String) {
        Toast.makeText(
            getApplication(),
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}