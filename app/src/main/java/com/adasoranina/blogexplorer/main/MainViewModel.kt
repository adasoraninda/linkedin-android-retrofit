package com.adasoranina.blogexplorer.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adasoranina.blogexplorer.api.RetrofitInstance
import com.adasoranina.blogexplorer.models.Post
import com.adasoranina.blogexplorer.utils.fetch

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private var currentPage = 1

    fun getPost() {
        Log.i(TAG, "Query with page $currentPage")
        _errorMessage.value = null
        _isLoading.value = true

        RetrofitInstance.api.getPosts(currentPage).fetch({ response ->
            _isLoading.value = false
            val fetchedPosts = response ?: emptyList()
            currentPage += 1
            Log.i(TAG, "Fetched posts: $fetchedPosts")
            val currentPosts = _posts.value ?: emptyList()
            _posts.value = currentPosts + fetchedPosts
        }, { error ->
            _isLoading.value = false
            _errorMessage.value = error.message
            Log.e(TAG, "Exception ${error.message}")
        })
    }

}