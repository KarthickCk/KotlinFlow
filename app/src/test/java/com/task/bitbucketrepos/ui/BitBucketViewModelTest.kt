package com.task.bitbucketrepos.ui

import com.task.bitbucketrepos.MainCoroutineRule
import com.task.bitbucketrepos.domain.BitBucketData
import com.task.bitbucketrepos.domain.IBitBucketRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

class BitBucketViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = MainCoroutineRule()

    @MockK
    lateinit var iBitBucketRepository: IBitBucketRepository

    private lateinit var bitBucketViewModel: BitBucketViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        bitBucketViewModel = BitBucketViewModel(iBitBucketRepository)
    }

    @Test
    fun testGetRepoListData() = runBlockingTest {
        val result = mutableListOf<BitBucketViewModel.UIState>()
        coEvery { iBitBucketRepository.getBitBucketList() } returns flow {
            emit(
                listOf(
                    BitBucketData.RepoData(
                        "12", "la",
                        BitBucketData.Links(BitBucketData.Links.Avatar("link")),
                        BitBucketData.Project("name")
                    )
                )
            )
        }

        val job = launch {
            bitBucketViewModel.listData.toList(result)
        }

        bitBucketViewModel.getRepoListData()

        Assert.assertEquals((result[0] as BitBucketViewModel.UIState.RepoList).list.size, 0)
        Assert.assertEquals((result[1] as BitBucketViewModel.UIState.Loading).isLoading, true)
        Assert.assertEquals((result[2] as BitBucketViewModel.UIState.RepoList).list.size, 1)
        Assert.assertEquals((result[3] as BitBucketViewModel.UIState.Loading).isLoading, false)

        job.cancel()
    }

    @Test
    fun testErrorGetRepoListData() = runBlockingTest {
        val result = mutableListOf<BitBucketViewModel.UIState>()
        coEvery { iBitBucketRepository.getBitBucketList() } returns flow {
            throw Exception("Error")
        }

        val job = launch {
            bitBucketViewModel.listData.toList(result)
        }

        bitBucketViewModel.getRepoListData()

        Assert.assertEquals((result[0] as BitBucketViewModel.UIState.RepoList).list.size, 0)
        Assert.assertEquals((result[1] as BitBucketViewModel.UIState.Loading).isLoading, true)
        Assert.assertEquals((result[2] as BitBucketViewModel.UIState.Loading).isLoading, false)
        Assert.assertEquals((result[3] as BitBucketViewModel.UIState.Error).throwable.message, "Error")

        job.cancel()
    }
}