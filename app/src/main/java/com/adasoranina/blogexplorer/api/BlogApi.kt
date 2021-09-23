package com.adasoranina.blogexplorer.api

import com.adasoranina.blogexplorer.models.Post
import com.adasoranina.blogexplorer.models.User
import retrofit2.Call
import retrofit2.http.*

interface BlogApi {

    @GET("posts")
    fun getPosts(
        @Query("_page") page: Int = 1,
        @Query("_limit") limit: Int = 10
    ): Call<List<Post>>

    @Headers("Platform: Android")
    @GET("posts/{id}")
    fun getPost(
        @Path("id") postId: Int
    ): Call<Post>

    @GET("users/{id}")
    fun getUser(
        @Path("id") userId: Int
    ): Call<User>

    @PUT("posts/{id}")
    fun updatePost(
        @Path("id") postId: Int,
        @Body post: Post
    ): Call<Post>

    @PATCH("posts/{id}")
    fun patchPost(
        @Path("id") postId: Int,
        @Body params: Map<String, String>
    ): Call<Post>

    @DELETE("posts/{id}")
    fun deletePost(
        @Header("Auth-Token") auth: String,
        @Path("id") postId: Int
    ): Call<Unit>

    @POST("posts/")
    fun createPost(
        @Body post: Post
    ): Call<Post>

    @FormUrlEncoded
    @POST("posts/")
    fun createPostUrlEncode(
        @Field("userId") userId: Int,
        @Field("title") title: String,
        @Field("body") body: String
    ): Call<Post>
}