package com.maryang.fastrxjava.data.repository

import com.maryang.fastrxjava.data.source.ApiManager
import com.maryang.fastrxjava.entity.GithubRepo
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.Call

class GithubRepository {

    private val api = ApiManager.githubApi

    fun getGithubRepos(): Call<List<GithubRepo>> =
        api.getRepos()

    fun getGithubReposWithMaybe(): Maybe<List<GithubRepo>> = api.getReposWithMaybe()

    fun getGithubReposWithCompletable(): Completable = api.getReposWithCompletable()

    fun getGithubReposWithSingle(): Single<List<GithubRepo>> = api.getReposWithSingle()
}
