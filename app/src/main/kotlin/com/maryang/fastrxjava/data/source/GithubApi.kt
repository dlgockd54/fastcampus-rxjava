package com.maryang.fastrxjava.data.source

import com.google.gson.JsonElement
import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.entity.User
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface GithubApi {

    @GET("users/{username}/repos")
    fun searchUserGithubRepos(
        @Path("username") userName: String,
        @Query("per_page") perPage: Int = 100
    ): Single<List<GithubRepo>>

    @GET("search/repositories")
    fun searchRepos(
        @Query("q") search: String
    ): Single<JsonElement>

    @GET("user/starred/{owner}/{repo}")
    fun checkStar(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Completable

    @PUT("user/starred/{owner}/{repo}")
    fun star(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Completable

    @DELETE("user/starred/{owner}/{repo}")
    fun unstar(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Completable

    @GET("users/{username}/followers")
    fun getUserFollowerList(
        @Path("username") userName: String,
        @Query("per_page") perPage: Int = 100
    ): Single<List<User>>

    @GET("users/{username}/following")
    fun getUserFollowingList(
        @Path("username") userName: String,
        @Query("per_page") perPage: Int = 100
    ): Single<List<User>>

    @GET("user/following/{username}")
    fun getUserFollowingState(
        @Path("username") userName: String
    ): Completable

    @PUT("user/following/{username}")
    fun followUser(
        @Path("username") userName: String
    ): Completable

    @DELETE("user/following/{username}")
    fun unfollowUser(
        @Path("username") userName: String
    ): Completable
}