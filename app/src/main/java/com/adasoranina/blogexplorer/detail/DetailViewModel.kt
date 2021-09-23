package com.adasoranina.blogexplorer.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adasoranina.blogexplorer.api.RetrofitInstance
import com.adasoranina.blogexplorer.models.Post
import com.adasoranina.blogexplorer.models.User
import com.adasoranina.blogexplorer.utils.fetch

private const val TAG = "DetailViewModel"

class DetailViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post>
        get() = _post

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun getPostDetail(postId: Int) {
        _isLoading.value = true
        RetrofitInstance.api.getPost(postId).fetch({ post ->
            if (post != null) {
                RetrofitInstance.api.getUser(post.userId).fetch({ fetchedUser ->
                    _isLoading.value = true
                    Log.i(TAG, "Fetched user: $fetchedUser")

                    _post.value = post
                    _user.value = fetchedUser
                    _isLoading.value = false
                }, { error ->
                    Log.e(TAG, "Exception: ${error.message}")
                    _isLoading.value = false
                })
            }
        }, { error ->
            Log.e(TAG, "Exception: ${error.message}")
            _isLoading.value = false
        })
    }

}