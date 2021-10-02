package com.example.newsapi.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.R
import com.example.newsapi.adapters.NewsListAdapter
import com.example.newsapi.databinding.FragmentSavedNewsBinding
import com.example.newsapi.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SavedNews : Fragment() {

    private lateinit var binding: FragmentSavedNewsBinding
    private val viewModel: NewsViewModel by viewModels()

    private val newsListAdapter = NewsListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedNewsBinding.inflate(layoutInflater, container, false)

        Log.i("@@@@@@", "Saved fragment")

        viewModel.refresh()

        setUpRecyclerView()

        observeViewModel()

        newsListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("article" ,it)
            }
            findNavController().navigate(
                R.id.action_savedNews_to_articleFragment,
                bundle
            )
        }

        implementDeletion()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.getSavedArticles().observe(viewLifecycleOwner, { article ->
            article?.let {
                binding.savedNewsRecyclerView.visibility = View.VISIBLE
                newsListAdapter.differ.submitList(it)
            }
        })
    }

    private fun implementDeletion() {
        val itemTouchHelper = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean { return true }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsListAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)

                Snackbar.make(view!!, "Article Deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") { viewModel.saveArticle(article) }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(binding.savedNewsRecyclerView)
        }
    }

    private fun setUpRecyclerView() {
        binding.savedNewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsListAdapter
        }
    }
}