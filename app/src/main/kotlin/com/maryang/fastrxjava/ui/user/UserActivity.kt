package com.maryang.fastrxjava.ui.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.maryang.fastrxjava.R
import com.maryang.fastrxjava.base.BaseActivity
import com.maryang.fastrxjava.base.BaseApplication
import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.entity.User
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_user.*
import org.jetbrains.anko.imageResource
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

        tv_follow.setOnClickListener {
            clickFollow(intent.getParcelableExtra<GithubRepo.GithubRepoUser>(KEY_USER).userName)
        }
        iv_follow.setOnClickListener {
            clickFollow(intent.getParcelableExtra<GithubRepo.GithubRepoUser>(KEY_USER).userName)
        }

        intent.getParcelableExtra<GithubRepo.GithubRepoUser>(KEY_USER).userName.let {
            subscribeUserInfo(it)
        }
    }

    private fun clickFollow(userName: String) {
        compositeDisposable +=
            mGithubUserViewModel.onClickFollow(
                tv_follow.text.equals(getString(R.string.follow)),
                userName
            ).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        toggleFollowingState(tv_follow.text.equals(getString(R.string.follow)))
                    }

                    override fun onError(e: Throwable) {
                        toggleFollowingState(!tv_follow.text.equals(getString(R.string.follow)))
                    }
                })
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

        compositeDisposable += getUserFollowingStateCompletable(userName)
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onComplete() {
                    toggleFollowingState(true)
                }

                override fun onError(e: Throwable) {
                    toggleFollowingState(false)
                }
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

    private fun getUserFollowingStateCompletable(userName: String): Completable =
        mGithubUserViewModel
            .getUserFollowingStateCompletable(userName)
            .observeOn(AndroidSchedulers.mainThread())

    private fun toggleFollowingState(follow: Boolean) {
        Log.d(BaseApplication.TAG, "toggleFollowState()")

        if (follow) {
            iv_follow.imageResource = R.drawable.ic_person_outline_black_24dp
            tv_follow.text = getString(R.string.unfollow)
            tv_follower_num.text = (tv_follower_num.text.toString().toInt() + 1).toString()
        } else {
            iv_follow.imageResource = R.drawable.ic_person_black_24dp
            tv_follow.text = getString(R.string.follow)
            tv_follower_num.text = (tv_follower_num.text.toString().toInt() - 1).toString()
        }
    }

    private fun showLoading() {
        user_info_loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        user_info_loading.visibility = View.GONE
    }
}
