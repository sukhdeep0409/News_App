package com.example.newsapi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.newsapi.R
import com.example.newsapi.databinding.FragmentArticleBinding
import com.example.newsapi.models.Article
import com.example.newsapi.viewmodels.NewsViewModel

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding

    private val args by navArgs<ArticleFragmentArgs>()
    private val viewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArticleBinding.inflate(layoutInflater, container, false)

        val article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            Toast.makeText(
                requireContext(),
                "News Added",
                Toast.LENGTH_LONG
            ).show()
        }

        return binding.root
    }
}
