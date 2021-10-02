package com.example.newsapi.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapi.R
import com.example.newsapi.adapters.NewsListAdapter
import com.example.newsapi.databinding.FragmentBreakingNewsBinding
import com.example.newsapi.viewmodels.NewsViewModel

class BreakingNews : Fragment() {

    private lateinit var binding: FragmentBreakingNewsBinding
    private val viewModel: NewsViewModel by viewModels()

    private val newsListAdapter = NewsListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreakingNewsBinding
            .inflate(layoutInflater, container, false)

        Log.i("@@@@@@", "Breaking News fragment")

        //viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        viewModel.refresh()

        setUpRecyclerView()

        observeViewModel()

        newsListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNews_to_articleFragment,
                bundle
            )
        }

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.breakingNews.observe(viewLifecycleOwner, { news ->
            news?.let { newsListAdapter.differ.submitList(it) }
        })
    }

    private fun setUpRecyclerView() {
        binding.breakingNewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsListAdapter
        }
    }
}