package com.task.bitbucketrepos.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.task.bitbucketrepos.R
import com.task.bitbucketrepos.domain.BitBucketData

class RepoListViewAdapter(
    private var list: MutableList<BitBucketData.RepoData>,
    private val onListClick: OnListClick
) : RecyclerView.Adapter<RepoListViewAdapter.RepoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_repo_list_item, parent, false)
        return RepoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.language.text = holder.imageView.context
            .getString(R.string.language, list[position].language)
        holder.title.text = list[position].project.name
        Glide.with(holder.itemView.context)
            .load(list[position].links.avatar.imageUrl)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            onListClick.onClick(list[position])
        }
    }

    override fun getItemCount() = list.size

    fun notifyList(list: List<BitBucketData.RepoData>) {
        val diffCallback = RepoDiffCallback(this.list, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.list.clear()
        this.list.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val language: TextView = itemView.findViewById(R.id.language)
        val title: TextView = itemView.findViewById(R.id.title)
        val imageView: ImageView = itemView.findViewById(R.id.repo_image)
    }

    class RepoDiffCallback(
        private val oldList: List<BitBucketData.RepoData>,
        private val newList: List<BitBucketData.RepoData>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].uuid === newList[newItemPosition].uuid
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            val (_, value, name) = oldList[oldPosition]
            val (_, value1, name1) = newList[newPosition]

            return name == name1 && value == value1
        }

    }

    interface OnListClick {
        fun onClick(repoData: BitBucketData.RepoData)
    }
}