package com.example.newsapi.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapi.R
import com.example.newsapi.adapters.NewsListAdapter
import com.example.newsapi.databinding.FragmentSearchNewsBinding
import com.example.newsapi.models.Article
import com.example.newsapi.viewmodels.NewsViewModel

class SearchNews : Fragment() {

    private lateinit var binding: FragmentSearchNewsBinding
    private val viewModel: NewsViewModel by viewModels()

    private val newsListAdapter = NewsListAdapter()

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = FragmentSearchNewsBinding.inflate(layoutInflater, container, false)

         setUpRecyclerView()

         binding.searchQuery.addTextChangedListener(object: TextWatcher {
             override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

             override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

             override fun afterTextChanged(editable: Editable?) {
                 filter(editable.toString())
             }
         })

         newsListAdapter.setOnItemClickListener {
             val bundle = Bundle().apply {
                 putParcelable("article", it)
             }
             findNavController().navigate(
                 R.id.action_searchNews_to_articleFragment,
                 bundle
             )
         }

         return binding.root
    }

    private fun filter(query: String) {
        viewModel.searchNews(query)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.searchedNews.observe(viewLifecycleOwner, { article ->
            article?.let {
                newsListAdapter.differ.submitList(it)
            }
        })
    }

    private fun setUpRecyclerView() {
        binding.searchNewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsListAdapter
        }
    }
}