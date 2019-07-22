package com.maryang.fastrxjava.ui.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maryang.fastrxjava.R
import com.maryang.fastrxjava.entity.GithubRepo
import kotlinx.android.synthetic.main.item_github_repo.view.*
import org.jetbrains.anko.imageResource

/**
 * Created by hclee on 2019-07-22.
 */

class UserRepoAdapter :
    RecyclerView.Adapter<UserRepoAdapter.UserRepoViewHolder>() {
    var mGitHubRepoList: List<GithubRepo> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRepoViewHolder =
        UserRepoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_github_repo, parent, false))

    override fun onBindViewHolder(holder: UserRepoViewHolder, position: Int) {
        holder.bind(mGitHubRepoList[position])
    }

    override fun getItemCount(): Int = mGitHubRepoList.size

    class UserRepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(repo: GithubRepo) {
            with(itemView) {
                repoName.text = repo.name
                repoDescription.text = repo.description
                repoStar.imageResource =
                    if (repo.star) R.drawable.baseline_star_24
                    else R.drawable.baseline_star_border_24
            }
        }
    }
}