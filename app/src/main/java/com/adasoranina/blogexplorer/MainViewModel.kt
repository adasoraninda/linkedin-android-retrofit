package com.adasoranina.blogexplorer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adasoranina.blogexplorer.api.RetrofitInstance
import com.adasoranina.blogexplorer.models.Post
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            Log.i(TAG, "Query with page $currentPage")
            _errorMessage.value = null
            _isLoading.value = true

            try {
                val fetchedPosts = RetrofitInstance.api.getPosts(currentPage)
                currentPage += 1
                Log.i(TAG, "Fetched posts: $fetchedPosts")
                val currentPosts = _posts.value ?: emptyList()
                _posts.value = currentPosts + fetchedPosts
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "Exception $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

}