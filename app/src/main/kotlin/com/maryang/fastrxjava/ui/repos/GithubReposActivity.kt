package com.maryang.fastrxjava.ui.repos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.maryang.fastrxjava.R
import com.maryang.fastrxjava.base.BaseViewModelActivity
import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.event.DataObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_github_repos.*


class GithubReposActivity : BaseViewModelActivity() {

    override val viewModel: GithubReposViewModel by lazy {
        GithubReposViewModel()
    }
    private val adapter: GithubReposAdapter by lazy {
        GithubReposAdapter()
    }
    private lateinit var mBackPressSubject: BehaviorSubject<Long>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_repos)

        mBackPressSubject = BehaviorSubject.createDefault(System.currentTimeMillis())

        compositeDisposable +=
            mBackPressSubject
                .observeOn(AndroidSchedulers.mainThread())
                .buffer(2, 1)
                .map {
                    Pair(it[0], it[1])
                }
                .subscribeWith(object : DisposableObserver<Pair<Long, Long>>() {
                    override fun onNext(t: Pair<Long, Long>) {
                        if (t.second - t.first <= 1500) {
                            finish()
                        } else {
                            Toast.makeText(this@GithubReposActivity, getString(R.string.back_press), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onComplete() {}
                    override fun onError(e: Throwable) {}
                })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = this.adapter

        refreshLayout.setOnRefreshListener { viewModel.searchGithubRepos() }

        searchText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(text: Editable?) {
                viewModel.searchGithubRepos(text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        subscribeSearch()
        subscribeDataObserver()
    }

    private fun subscribeSearch() {
        compositeDisposable += viewModel.searchGithubReposSubject()
            .doOnNext {
                if (it) showLoading()
            }
            .switchMap { viewModel.searchGithubReposObservable() }
            .subscribeWith(object : DisposableObserver<List<GithubRepo>>() {
                override fun onNext(t: List<GithubRepo>) {
                    hideLoading()
                    adapter.items = t
                }

                override fun onComplete() {
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                }
            })
    }

    private fun subscribeDataObserver() {
        compositeDisposable += DataObserver.observe()
            .filter { it is GithubRepo }
            .subscribe { repo ->
                adapter.items.find {
                    it.id == repo.id
                }?.apply {
                    star = star.not()
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        refreshLayout.isRefreshing = false
    }

    override fun onBackPressed() {
        mBackPressSubject.onNext(System.currentTimeMillis())
    }
}
