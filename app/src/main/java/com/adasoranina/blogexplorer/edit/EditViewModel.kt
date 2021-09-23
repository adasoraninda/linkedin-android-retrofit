package com.adasoranina.blogexplorer.edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adasoranina.blogexplorer.api.RetrofitInstance
import com.adasoranina.blogexplorer.models.Post
import com.adasoranina.blogexplorer.utils.fetch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "EditViewModel"

class EditViewModel : ViewModel() {

    private val _post: MutableLiveData<Post?> = MutableLiveData()
    val post: LiveData<Post?>
        get() = _post

    private val _currentStatus = MutableLiveData(ResultStatus.IDLE)
    val currentStatus: LiveData<ResultStatus>
        get() = _currentStatus

    private val _wasDeletionSuccessful = MutableLiveData(false)
    val wasDeletionSuccessful: LiveData<Boolean>
        get() = _wasDeletionSuccessful

    fun updatePost(postId: Int, newPostData: Post) {
        RetrofitInstance.api.updatePost(postId, newPostData).fetch({ updatedPost ->
            if (updatedPost != null) {
                _post.value = null
                _currentStatus.value = ResultStatus.WORKING
                Log.i(TAG, "Updated post: $updatedPost")
                _post.value = updatedPost
                _currentStatus.value = ResultStatus.SUCCESS
            }
        }, { error ->
            Log.e(TAG, "Eception: ${error.message}")
            _currentStatus.value = ResultStatus.ERROR
        })
    }

    fun patchPost(postId: Int, title: String, body: String) {
        RetrofitInstance.api.patchPost(postId, mapOf("title" to title, "body" to body))
            .fetch({ patchedPost ->
                _post.value = null
                _currentStatus.value = ResultStatus.WORKING
                Log.i(TAG, "Patched post $patchedPost")
                _post.value = patchedPost
                _currentStatus.value = ResultStatus.SUCCESS
            }, { error ->
                Log.e(TAG, "Eception: ${error.message}")
                _currentStatus.value = ResultStatus.ERROR
            })
    }

    fun deletePost(postId: Int) {
        RetrofitInstance.api.deletePost("1234AuthToken", postId).fetch({
            _currentStatus.value = ResultStatus.WORKING
            _post.value = null
            _wasDeletionSuccessful.value = true
            _currentStatus.value = ResultStatus.SUCCESS
        }, { error ->
            Log.e(TAG, "Eception: ${error.message}")
            _currentStatus.value = ResultStatus.ERROR
        })
    }

    fun createPost(postData: Post) {
        RetrofitInstance.api.createPost(postData).fetch({ newPost ->
            newPost?.let { Log.i(TAG, "New post: $it") }
        })
    }

    fun createPostUrlEncoded(userId: Int, title: String, content: String) {
        RetrofitInstance.api.createPostUrlEncode(userId, title, content).fetch({ urlEncodePost ->
            urlEncodePost?.let { Log.i(TAG, "URL encoded post: $it") }
        })
    }

}