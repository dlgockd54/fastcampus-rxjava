package com.maryang.fastrxjava.ui

import android.util.Log
import com.maryang.fastrxjava.data.repository.GithubRepository
import com.maryang.fastrxjava.data.source.DefaultCallback
import com.maryang.fastrxjava.entity.GithubRepo
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response

class GithubReposViewModel {
    companion object {
        val TAG: String = GithubReposViewModel::class.java.simpleName
    }

    private val repository = GithubRepository()

    fun getGithubRepos(
        onResponse: (List<GithubRepo>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        repository.getGithubRepos().enqueue(object : DefaultCallback<List<GithubRepo>>() {
            override fun onResponse(
                call: Call<List<GithubRepo>>,
                response: Response<List<GithubRepo>>
            ) {
                onResponse(response.body() ?: emptyList())
            }

            override fun onFailure(call: Call<List<GithubRepo>>, t: Throwable) {
                super.onFailure(call, t)
                onFailure(t)
            }
        })
    }

    fun getGithubReposWithMaybe(): Maybe<List<GithubRepo>> {
        return repository.getGithubReposWithMaybe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getGithubReposWithCompletable(): Completable {
        return repository.getGithubReposWithCompletable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getGithubReposWithSingle(): Single<List<GithubRepo>> {
        return repository.getGithubReposWithSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
