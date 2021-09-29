package com.adasoranina.blogexplorer.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.adasoranina.blogexplorer.main.EXTRA_POST_ID
import com.adasoranina.blogexplorer.R
import com.adasoranina.blogexplorer.databinding.ActivityDetailBinding
import com.adasoranina.blogexplorer.edit.EditActivity


const val EXTRA_POST = "EXTRA_POST"
private const val TAG = "DetailActivity"

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding: ActivityDetailBinding? get() = _binding

    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        viewModel.isLoading.observe(this) { isLoading ->
            binding?.detailProgressBar?.isVisible = isLoading
            binding?.clContent?.isVisible = !isLoading
        }

        viewModel.post.observe(this) { post ->
            binding?.tvPostId?.text = "Post #${post.id}"
            binding?.tvTitle?.text = post.title
            binding?.tvBody?.text = post.content
        }

        viewModel.user.observe(this) { user ->
            binding?.tvUserName?.text = user.name
            binding?.tvUserEmail?.text = user.email
            binding?.tvUsername?.text = user.username
            binding?.tvWebsite?.text = user.website
        }

        viewModel.getPostDetail(intent.getIntExtra(EXTRA_POST_ID, -1))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.miEdit) {
            Log.i(TAG, "Navigate to edit screen")
            viewModel.post.observe(this) { post ->
                val intent = Intent(this, EditActivity::class.java)
                intent.putExtra(EXTRA_POST, post)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}