package com.adasoranina.blogexplorer.utils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <Result : Any> Call<Result>.fetch(
    successBody: (response: Result?) -> Unit,
    errorBody: ((error: Throwable) -> Unit)? = null
) {
    enqueue(object : Callback<Result> {
        override fun onResponse(call: Call<Result>, response: Response<Result>) {
            if (response.isSuccessful && response.code() == 200 && response.body() != null) {
                successBody(response.body())
            }
        }

        override fun onFailure(call: Call<Result>, t: Throwable) {
            errorBody?.invoke(t)
        }
    })
}