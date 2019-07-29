package com.maryang.fastrxjava.ui.user

import com.maryang.fastrxjava.base.BaseViewModel
import com.maryang.fastrxjava.data.repository.GithubRepository
import com.maryang.fastrxjava.data.source.FollowingStateEnum
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by hclee on 2019-07-23.
 */

class GithubUserViewModel : BaseViewModel() {
    private val repository: GithubRepository = GithubRepository()

    fun getUserFollowerListSingle(userName: String) =
        repository.getUserFollowerList(userName)

    fun getUserFollowingListSingle(userName: String) =
        repository.getUserFollowingList(userName)

    fun getUserGithubReposSingle(userName: String) =
        repository.searchUserGithubRepos(userName)

    fun getUserFollowingStateCompletable(userName: String): Completable =
        repository.getUserFollowingStateCompletable(userName)

    fun onClickFollow(isFollowing: Boolean, userName: String): Single<FollowingStateEnum> =
        if(isFollowing) repository.unfollowUser(userName) else repository.followUser(userName)
}