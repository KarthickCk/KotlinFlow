package com.task.bitbucketrepos.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.bitbucketrepos.R
import com.task.bitbucketrepos.domain.BitBucketData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RepoListViewAdapter.OnListClick {

    private val bitBucketViewModel: BitBucketViewModel by viewModels()
    private val repoListViewAdapter = RepoListViewAdapter(mutableListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViews()
        observeData()
        bitBucketViewModel.getRepoListData()
    }

    private fun setViews() {
        repo_list.layoutManager = LinearLayoutManager(this)
        repo_list.adapter = repoListViewAdapter
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                bitBucketViewModel.listData.collect {
                    when (it) {
                        is BitBucketViewModel.UIState.RepoList -> {
                            repoListViewAdapter.notifyList(it.list)
                        }

                        is BitBucketViewModel.UIState.Loading -> {
                            progress_bar.visibility = if (it.isLoading) View.VISIBLE else View.GONE
                        }

                        is BitBucketViewModel.UIState.Error -> {
                            Toast.makeText(
                                this@MainActivity,
                                it.throwable.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onClick(repoData: BitBucketData.RepoData) {
        startActivity(DetailsActivity.newIntent(this, repoData))
    }
}