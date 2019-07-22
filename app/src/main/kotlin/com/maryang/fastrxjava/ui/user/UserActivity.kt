package com.maryang.fastrxjava.ui.user

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.maryang.fastrxjava.base.BaseActivity
import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.ui.repos.GithubReposViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_user.*
import org.jetbrains.anko.intentFor


class UserActivity : BaseActivity() {

    companion object {
        private const val KEY_USER = "KEY_USER"

        fun start(context: Context, user: GithubRepo.GithubRepoUser) {
            context.startActivity(
                context.intentFor<UserActivity>(
                    KEY_USER to user
                )
            )
        }
    }

    private lateinit var mUserRepoAdapter: UserRepoAdapter
    private lateinit var mGithubReposViewModel: GithubReposViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.maryang.fastrxjava.R.layout.activity_user)
        intent.getParcelableExtra<GithubRepo.GithubRepoUser>(KEY_USER).let {
            supportActionBar?.run {
                title = it.userName
                setDisplayHomeAsUpEnabled(true)
            }

            showUserInfo(it)
        }

        mGithubReposViewModel = GithubReposViewModel()
        mUserRepoAdapter = UserRepoAdapter()

        with(user_repo_list) {
            adapter = mUserRepoAdapter
            layoutManager = LinearLayoutManager(this@UserActivity)
        }

        subscribeUserRepoList(intent.getParcelableExtra<GithubRepo.GithubRepoUser>(KEY_USER).userName)
    }

    private fun showUserInfo(user: GithubRepo.GithubRepoUser) {
        Glide.with(this@UserActivity)
            .load(user.avatarUrl)
            .into(ownerImage)
        ownerName.text = user.userName
    }

    private fun subscribeUserRepoList(userName: String) {
        compositeDisposable += mGithubReposViewModel
            .getUserGithubReposSingle(userName)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<List<GithubRepo>>() {
                override fun onSuccess(t: List<GithubRepo>) {
                    mUserRepoAdapter.mGitHubRepoList = t
                }

                override fun onError(e: Throwable) {

                }
            })
    }
}
