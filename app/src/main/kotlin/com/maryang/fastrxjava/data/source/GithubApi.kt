package com.maryang.fastrxjava.data.source

import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.entity.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {

    @GET("users/{userName}/repos")
    fun getRepos(
        @Path("userName") userName: String = "googlesamples"
    ): Call<List<GithubRepo>>

    @GET("users/{userName}")
    fun getUser(
        @Path("userName") userName: String = "octocat"
    ): Call<User>

    @GET("users/{userName}/repos")
    fun getReposWithMaybe(
        @Path("userName") userName: String = "googlesamples"
    ): Maybe<List<GithubRepo>>

    @GET("users/{userName}/repos")
    fun getReposWithCompletable(
            @Path("userName") userName: String = "googlesamples"
    ): Completable

    @GET("users/{userName}/repos")
    fun getReposWithSingle(
            @Path("userName") userName: String = "googlesamples"
    ): Single<List<GithubRepo>>
}
