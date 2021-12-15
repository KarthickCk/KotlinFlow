package com.task.bitbucketrepos.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.task.bitbucketrepos.R
import com.task.bitbucketrepos.domain.BitBucketData
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        intent.getParcelableExtra<BitBucketData.RepoData>(REPO_DATA)?.apply {
            repo_title.text = project.name
            Glide.with(this@DetailsActivity)
                .load(links.avatar.imageUrl)
                .into(image_view)
        }
    }

    companion object {

        private const val REPO_DATA = "repo_data"

        fun newIntent(context: Context, repoData: BitBucketData.RepoData): Intent {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(REPO_DATA, repoData)
            return intent
        }
    }
}