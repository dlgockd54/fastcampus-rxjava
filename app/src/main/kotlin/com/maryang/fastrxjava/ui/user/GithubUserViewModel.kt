package com.maryang.fastrxjava.ui.user

import com.maryang.fastrxjava.base.BaseViewModel
import com.maryang.fastrxjava.data.repository.GithubRepository

/**
 * Created by hclee on 2019-07-23.
 */

class GithubUserViewModel : BaseViewModel() {
    private val repository: GithubRepository = GithubRepository()

    fun getUserFollowerListSingle(userName: String) =
        repository.getUserFollowerList(userName)

    fun getUserFollowingListSingle(userName: String) =
        repository.getUserFollowingList(userName)
}