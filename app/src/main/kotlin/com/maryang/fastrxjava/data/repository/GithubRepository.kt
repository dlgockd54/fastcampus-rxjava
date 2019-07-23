package com.maryang.fastrxjava.data.repository

import com.maryang.fastrxjava.data.source.ApiManager
import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.entity.User
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers


class GithubRepository {

    private val api = ApiManager.githubApi

    fun searchUserGithubRepos(userName: String): Single<List<GithubRepo>> =
        api.searchUserGithubRepos(userName)
            .subscribeOn(Schedulers.io())

    fun searchGithubRepos(q: String): Single<List<GithubRepo>> =
        api.searchRepos(q)
            .map {
                it.asJsonObject.getAsJsonArray("items")
                    .map { repo ->
                        ApiManager.gson.fromJson(repo, GithubRepo::class.java)!!
                    }
            }
            .subscribeOn(Schedulers.io())

    fun checkStar(owner: String, repo: String): Completable =
        api.checkStar(owner, repo)
            .subscribeOn(Schedulers.io())

    fun star(owner: String, repo: String): Completable =
        api.star(owner, repo)
            .subscribeOn(Schedulers.io())

    fun unstar(owner: String, repo: String): Completable =
        api.unstar(owner, repo)
            .subscribeOn(Schedulers.io())

    fun getUserFollowerList(userName: String): Single<List<User>> =
        api.getUserFollowerList(userName)
            .subscribeOn(Schedulers.io())

    fun getUserFollowingList(userName: String): Single<List<User>> =
        api.getUserFollowingList(userName)
            .subscribeOn(Schedulers.io())
}
