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
        RetrofitInstance.api.getPosts(currentPage).fetch({
            Log.i(TAG, "Query with page $currentPage")
            _errorMessage.value = null
            _isLoading.value = true
        }, { response, error ->

            if (error != null) {
                Log.e(TAG, "Exception ${error.message}")
                _errorMessage.value = error.message
            }

            if (response != null) {
                Log.i(TAG, "Fetched posts: $response")
                currentPage += 1
                val currentPosts = _posts.value ?: emptyList()
                _posts.value = currentPosts + response
            }

            _isLoading.value = false
        })
    }

}