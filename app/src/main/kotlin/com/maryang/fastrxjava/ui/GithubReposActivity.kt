package com.maryang.fastrxjava.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.maryang.fastrxjava.R
import kotlinx.android.synthetic.main.activity_github_repos.*


class GithubReposActivity : AppCompatActivity() {
    companion object {
        val TAG: String = GithubReposActivity::class.java.simpleName
    }

    private val viewModel: GithubReposViewModel by lazy {
        GithubReposViewModel()
    }
    private val adapter: GithubReposAdapter by lazy {
        GithubReposAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_repos)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = this.adapter

        refreshLayout.setOnRefreshListener { load() }

//        load(true)
//        loadWithMaybe(true)
//        loadWithCompletable(true)
        loadWithSingle(true)
    }

    private fun load(showLoading: Boolean = false) {
        if (showLoading)
            showLoading()
        viewModel.getGithubRepos(
            {
                hideLoading()
                adapter.items = it
            },
            {
                hideLoading()
            }
        )
    }

    private fun loadWithMaybe(showLoading: Boolean = false) {
        if(showLoading) {
            showLoading()
        }
        viewModel.getGithubReposWithMaybe()
                .subscribe({
                    Log.d(TAG, "onSuccess()")

                    hideLoading()
                    adapter.items = it
                }, {
                    Log.d(TAG, "onError()")
                    hideLoading()
                }, {
                    Log.d(TAG, "onComplete()")
                    Log.d(TAG, "return value is null")
                    hideLoading()
                })
    }

    private fun loadWithCompletable(showLoading: Boolean = false) {
        if(showLoading) {
            showLoading()
        }

        viewModel.getGithubReposWithCompletable()
                .subscribe({
                    Log.d(TAG, "onComplete()")
                    hideLoading()
                }, {
                    Log.d(TAG, "onError()")
                    Log.d(TAG, it.message)
                    hideLoading()
                })
    }

    private fun loadWithSingle(showLoading: Boolean = false) {
        if(showLoading) {
            showLoading()
        }

        viewModel.getGithubReposWithSingle()
                .subscribe({
                    Log.d(TAG, "onSuccess()")

                    adapter.items = it
                    hideLoading()
                }, {
                    Log.d(TAG, "onError()")
                    Log.d(TAG, it.message)
                    hideLoading()
                })
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        refreshLayout.isRefreshing = false
    }
}
