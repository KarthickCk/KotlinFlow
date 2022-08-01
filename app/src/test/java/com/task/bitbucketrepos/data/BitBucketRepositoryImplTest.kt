package com.task.bitbucketrepos.data

import com.task.bitbucketrepos.domain.BitBucketApi
import com.task.bitbucketrepos.domain.BitBucketData
import com.task.bitbucketrepos.domain.ICacheData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.lang.Exception

class BitBucketRepositoryImplTest {

    @MockK
    lateinit var bitBucketApi: BitBucketApi

    @MockK
    lateinit var ioDispatcher: CoroutineDispatcher

    @MockK
    lateinit var iCacheData: ICacheData

    private lateinit var bitBucketRepository: BitBucketRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        bitBucketRepository = BitBucketRepositoryImpl(bitBucketApi, ioDispatcher, iCacheData)
    }

    @Test
    fun `test Network Success Case`() = runBlockingTest {

        coEvery { bitBucketApi.getRepoList() } returns BitBucketData.mock()
        every { iCacheData.saveData(BitBucketData.mock().repoList) } returns Unit

        bitBucketRepository.getNetworkData().collectLatest {
            Assert.assertEquals(it.size, 1)
        }
    }

    @Test
    fun `test Network Success EmptyData`() = runBlockingTest {

        coEvery { bitBucketApi.getRepoList() } returns BitBucketData(repoList = emptyList())
        every { iCacheData.saveData(Mockito.anyList()) } returns Unit

        bitBucketRepository.getNetworkData()
            .collectLatest {
                Assert.assertEquals(it.size, 0)
            }
    }

    @Test
    fun `test Network With Exception`() = runBlockingTest {

        val exception = Exception("Exception")
        coEvery { bitBucketApi.getRepoList() } throws exception

        bitBucketRepository.getNetworkData().catch { cause ->
            Assert.assertNotNull(cause)
            Assert.assertEquals(cause, exception)
        }
    }

    @Test
    fun `test Cached Data`() = runBlockingTest {

        coEvery { iCacheData.getData() } returns BitBucketData.mock().repoList

        bitBucketRepository.getCachedData().collectLatest {
            Assert.assertEquals(it.size, 1)
        }
    }
}