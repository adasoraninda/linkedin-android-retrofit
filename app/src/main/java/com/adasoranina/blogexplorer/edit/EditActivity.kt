package com.adasoranina.blogexplorer.edit

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.adasoranina.blogexplorer.MainActivity
import com.adasoranina.blogexplorer.api.RetrofitInstance
import com.adasoranina.blogexplorer.databinding.ActivityEditBinding
import com.adasoranina.blogexplorer.detail.EXTRA_POST
import com.adasoranina.blogexplorer.models.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "EditActivity"

class EditActivity : AppCompatActivity() {

    private var _binding: ActivityEditBinding? = null
    private val binding: ActivityEditBinding? get() = _binding

    private lateinit var viewModel: EditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val post = intent.getSerializableExtra(EXTRA_POST) as Post
        title = "Editing Post #${post.id}"
        binding?.etTitle?.setText(post.title)
        binding?.etContent?.setText(post.content)

        viewModel = ViewModelProvider(this)[EditViewModel::class.java]

        viewModel.post.observe(this) { updatedPost ->
            if (updatedPost == null) {
                binding?.clPostResult?.visibility = View.GONE
                return@observe
            }

            binding?.tvUpdatedTitle?.text = updatedPost.title
            binding?.tvUpdatedContent?.text = updatedPost.content
            binding?.clPostResult?.visibility = View.VISIBLE
        }

        viewModel.currentStatus.observe(this) { currentStatus ->
            when (currentStatus) {
                ResultStatus.IDLE -> {
                    binding?.tvStatus?.text = "Idle"
                    binding?.tvStatus?.setTextColor(Color.GRAY)
                }
                ResultStatus.WORKING -> {
                    binding?.tvStatus?.text = "Working..."
                    binding?.tvStatus?.setTextColor(Color.MAGENTA)
                }
                ResultStatus.SUCCESS -> {
                    binding?.tvStatus?.text = "Success!"
                    binding?.tvStatus?.setTextColor(Color.GREEN)
                }
                ResultStatus.ERROR -> {
                    binding?.tvStatus?.text = "Error :("
                    binding?.tvStatus?.setTextColor(Color.RED)
                }
                else -> throw IllegalStateException("Unexpected result state found")
            }
        }

        viewModel.wasDeletionSuccessful.observe(this) { wasDeletionSuccessful ->
            if (wasDeletionSuccessful) {
                Toast.makeText(this, "Deleted post successfully", Toast.LENGTH_LONG).show()
                Intent(this, MainActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }

        binding?.btnUpdatePut?.setOnClickListener {
            Log.i(TAG, "Update via PUT")
            val title = binding?.etTitle?.text?.toString()
            val body = binding?.etContent?.text?.toString()

            if (title == null || body == null) {
                Toast.makeText(this, "Please fill title & body", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newPost = Post(
                userId = post.userId,
                id = post.id,
                title = title,
                content = body
            )
            viewModel.updatePost(post.id, newPost)
        }

        binding?.btnUpdatePatch?.setOnClickListener {
            val title = binding?.etTitle?.text?.toString()
            val body = binding?.etContent?.text?.toString()

            if (title == null || body == null) {
                Toast.makeText(this, "Please fill title & body", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.i(TAG, "Update via PATCH")
            viewModel.patchPost(
                postId = post.id,
                title = title,
                body = body
            )
        }

        binding?.btnDelete?.setOnClickListener {
            Log.i(TAG, "DELETE")
            viewModel.deletePost(postId = post.id)
        }
    }

    private fun createPost() {
        CoroutineScope(Dispatchers.IO).launch {
            val localNewPost = Post(2, 32, "My post title", "Body of post id #32")
            val newPost = RetrofitInstance.api.createPost(localNewPost)
            Log.i(TAG, "New post: $newPost")

            val urlEncodePost =
                RetrofitInstance.api.createPostUrlEncode(4, "New title", "Post content")
            Log.i(TAG, "URL encoded post: $urlEncodePost")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}