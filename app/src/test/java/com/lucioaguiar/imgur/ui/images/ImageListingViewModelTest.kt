package com.lucioaguiar.imgur.ui.images

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.lucioaguiar.imgur.data.models.Image
import com.lucioaguiar.imgur.data.repositories.GalleryRepository
import com.lucioaguiar.imgur.util.DEFAULT_SPAN_COUNT
import com.lucioaguiar.imgur.util.UiState
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ImageListingViewModelTest {

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

    private var galleryRepository: GalleryRepository = mockk()
    private var imagesObserver: Observer<UiState<List<Image>>> = mockk(relaxed = true)
    private var scrollEndObserver: Observer<Boolean> = mockk(relaxed = true)
    private var spanCountObserver: Observer<Int> = mockk(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when fetchImages is called then it should call repository to fetches images`() = runTest {
        val mockedList = UiState.Success(arrayListOf(Image("link1"), Image("link2")))
        coEvery { galleryRepository.getData(any(), any(), any(), any()) } returns mockedList
        instatiateViewModel()
        advanceTimeBy(2000)
        coVerify { galleryRepository.getData(any(), any(), any(), any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when repository takes is to complete then images should be updated`() = runTest {
        val mockedList = UiState.Success(arrayListOf(Image("link1"), Image("link2")))
        coEvery { galleryRepository.getData(any(), any(), any(), any()) } returns mockedList
        instatiateViewModel()
        advanceTimeBy(2000)
        coVerifyOrder {
            imagesObserver.onChanged(UiState.Loading)
            galleryRepository.getData(any(), any(), any(), any())
            imagesObserver.onChanged(mockedList)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when the scroll reaches the end then observer should return true`() = runTest {
        instatiateViewModel().recyclerViewScrollIsEnd()
        coVerifyOrder {
            scrollEndObserver.onChanged(true)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when the scroll reaches the end then observer should return false`() = runTest {
        instatiateViewModel().recyclerViewScrollIsNotEnd()
        coVerifyOrder {
            scrollEndObserver.onChanged(false)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when the span count for 2 then spanCount should changed`() = runTest {
        val viewModel = instatiateViewModel()
        viewModel.setSpanCount(2)
        assertEquals(viewModel.getSpanCount(), 2)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when the span count for 1 then spanCount should not change`() = runTest {
        val viewModel = instatiateViewModel()
        viewModel.setSpanCount(1)
        assertEquals(viewModel.getSpanCount(), DEFAULT_SPAN_COUNT)
    }

    private fun instatiateViewModel(): ImageListingViewModel {
        val viewmodel =  ImageListingViewModel(galleryRepository)
        viewmodel.images.observeForever(imagesObserver)
        viewmodel.scrollEnd.observeForever(scrollEndObserver)
        viewmodel.spanCount.observeForever(spanCountObserver)
        return viewmodel
    }

}