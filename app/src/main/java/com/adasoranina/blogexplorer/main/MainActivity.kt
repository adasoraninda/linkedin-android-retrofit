package com.adasoranina.blogexplorer.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adasoranina.blogexplorer.databinding.ActivityMainBinding
import com.adasoranina.blogexplorer.detail.DetailActivity

private const val TAG = "MainActivity"
const val EXTRA_POST_ID = "EXTRA_POST_ID"

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding? get() = _binding

    private lateinit var blogPostAdapter: BlogPostAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.isLoading.observe(this) { isLoading ->
            Log.i(TAG, "isLoading $isLoading")
            binding?.progressBar?.isVisible = isLoading
        }

        viewModel.posts.observe(this) { posts ->
            Log.i(TAG, "Number of posts: ${posts.size}")
            val numElements = blogPostAdapter.getCurrentPosts().size
            blogPostAdapter.updatePost(posts)
            binding?.rvPosts?.smoothScrollToPosition(numElements)
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            binding?.tvError?.isVisible = errorMessage != null

            errorMessage?.let {
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        blogPostAdapter = BlogPostAdapter { post ->
            Intent(this@MainActivity, DetailActivity::class.java).apply {
                putExtra(EXTRA_POST_ID, post.id)
                startActivity(this)
            }
        }
        binding?.rvPosts?.adapter = blogPostAdapter
        binding?.rvPosts?.layoutManager = LinearLayoutManager(this)

        binding?.button?.setOnClickListener {
            viewModel.getPost()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}