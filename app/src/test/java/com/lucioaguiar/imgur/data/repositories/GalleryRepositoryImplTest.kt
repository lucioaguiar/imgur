package com.lucioaguiar.imgur.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lucioaguiar.imgur.data.models.Data
import com.lucioaguiar.imgur.data.models.Gallery
import com.lucioaguiar.imgur.data.models.Image
import com.lucioaguiar.imgur.util.DEFAULT_PAGE
import com.lucioaguiar.imgur.util.DEFAULT_QUERY
import com.lucioaguiar.imgur.util.DEFAULT_SIZE
import com.lucioaguiar.imgur.util.DEFAULT_TYPE
import com.lucioaguiar.imgur.rest.RetrofitService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GalleryRepositoryImplTest{

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    private val retrofitService: RetrofitService = mockk()
    private val dataFake = Data(arrayListOf(Gallery(arrayListOf(Image("link1"), Image("link2")))), true, 200)
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when fetchGalleries is called than is should call service fetchGalleries`() = runTest {
        coEvery { retrofitService.galleries(any(), any(), any(), any()) } returns dataFake

        GalleryRepositoryImpl(retrofitService).getData(
            DEFAULT_PAGE, DEFAULT_QUERY, DEFAULT_TYPE, DEFAULT_SIZE)

        coVerify { retrofitService.galleries(any(), any(), any(), any()) }

    }


}