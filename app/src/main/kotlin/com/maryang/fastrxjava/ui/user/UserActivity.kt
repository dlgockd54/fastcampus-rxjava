package com.maryang.fastrxjava.ui.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.maryang.fastrxjava.base.BaseActivity
import com.maryang.fastrxjava.base.BaseApplication
import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.entity.User
import com.maryang.fastrxjava.ui.repos.GithubReposViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_user.*
import org.jetbrains.anko.intentFor
import org.reactivestreams.Subscriber


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
    private lateinit var mGithubUserViewModel: GithubUserViewModel

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

        mGithubUserViewModel = GithubUserViewModel()
        mUserRepoAdapter = UserRepoAdapter()

        with(user_repo_list) {
            adapter = mUserRepoAdapter
            layoutManager = LinearLayoutManager(this@UserActivity)
        }

        intent.getParcelableExtra<GithubRepo.GithubRepoUser>(KEY_USER).userName.let {
            subscribeUserInfo(it)
        }
    }

    private fun showUserInfo(user: GithubRepo.GithubRepoUser) {
        Glide.with(this@UserActivity)
            .load(user.avatarUrl)
            .into(ownerImage)
        ownerName.text = user.userName
    }

    private fun subscribeUserInfo(userName: String) {
        showLoading()

        compositeDisposable += Single.merge(
            getUserFollowerListSingle(userName),
            getUserFollowingListSingle(userName),
            getUserRepoListSingle(userName)
        )
            .subscribe({

            }, {
                Log.d(BaseApplication.TAG, it.message)
            }, {
                hideLoading()
            })
    }

    private fun getUserFollowerListSingle(userName: String): Single<List<User>> =
        mGithubUserViewModel
            .getUserFollowerListSingle(userName)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                Log.d("UserActivity", "follower success")
                tv_follower_num.text = it.size.toString()
            }

    private fun getUserFollowingListSingle(userName: String): Single<List<User>> =
        mGithubUserViewModel
            .getUserFollowingListSingle(userName)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                Log.d("UserActivity", "following success")
                tv_following_num.text = it.size.toString()
            }

    private fun getUserRepoListSingle(userName: String): Single<List<GithubRepo>> =
        mGithubUserViewModel
            .getUserGithubReposSingle(userName)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                Log.d("UserActivity", "repo list success")
                mUserRepoAdapter.mGitHubRepoList = it
            }

    private fun showLoading() {
        user_info_loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        user_info_loading.visibility = View.GONE
    }
}
