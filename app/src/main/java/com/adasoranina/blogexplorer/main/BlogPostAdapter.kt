package com.adasoranina.blogexplorer.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adasoranina.blogexplorer.databinding.ItemBlogPostBinding
import com.adasoranina.blogexplorer.models.Post

class BlogPostAdapter(private val onItemClick: ((post: Post) -> Unit)?) :
    RecyclerView.Adapter<BlogPostAdapter.PostViewHolder>() {

    private val listPost = mutableListOf<Post>()

    @SuppressLint("NotifyDataSetChanged")
    fun updatePost(newPosts: List<Post>) {
        listPost.clear()
        listPost.addAll(newPosts)
        notifyDataSetChanged()
    }

    fun getCurrentPosts(): List<Post> {
        return listPost
    }

    class PostViewHolder private constructor(private val binding: ItemBlogPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post, onItemClick: ((post: Post) -> Unit)?) {
            binding.let {
                it.tvId.text = "Post #${post.id}"
                it.tvTitle.text = post.title
                it.tvBlogBody.text = post.content
                it.root.setOnClickListener { onItemClick?.invoke(post) }
            }
        }

        companion object {
            fun from(parent: ViewGroup): PostViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemBlogPostBinding.inflate(inflater, parent, false)

                return PostViewHolder(binding)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(post = listPost[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return listPost.size
    }
}