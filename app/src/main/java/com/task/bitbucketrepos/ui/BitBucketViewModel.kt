package com.task.bitbucketrepos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.bitbucketrepos.domain.BitBucketData
import com.task.bitbucketrepos.domain.IBitBucketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BitBucketViewModel @Inject constructor(
    private val iBitBucketRepository: IBitBucketRepository
) : ViewModel() {

    private val repoList = MutableStateFlow<UIState>(UIState.RepoList(emptyList()))
    val listData: StateFlow<UIState> = repoList

    fun getRepoListData() {
        viewModelScope.launch {
            iBitBucketRepository.getBitBucketList()
                .onStart {
                    repoList.value = UIState.Loading(true)
                }
                .onCompletion {
                    repoList.value = UIState.Loading(false)
                }
                .catch {
                    repoList.value = UIState.Error(it)
                }
                .collect {
                    repoList.value = UIState.RepoList(it)
                }
        }
    }

    sealed class UIState {
        class RepoList(val list: List<BitBucketData.RepoData>): UIState()
        class Loading(val isLoading: Boolean): UIState()
        class Error(val throwable: Throwable): UIState()
    }
}