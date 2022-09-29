package com.lucioaguiar.imgur.ui.images

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucioaguiar.imgur.data.models.Image
import com.lucioaguiar.imgur.data.repositories.GalleryRepository
import com.lucioaguiar.imgur.util.*
import kotlinx.coroutines.launch

class ImageListingViewModel(private val repository: GalleryRepository) : ViewModel() {

    private var page = DEFAULT_PAGE
    private val query = DEFAULT_QUERY
    private val type = DEFAULT_TYPE
    private val size = DEFAULT_SIZE

    private val _images = MutableLiveData<UiState<List<Image>>>()
    val images: LiveData<UiState<List<Image>>> = _images

    private val _spanCount = MutableLiveData<Int>()
    val spanCount: LiveData<Int> = _spanCount

    private val _scrollEnd = MutableLiveData<Boolean>()
    val scrollEnd: LiveData<Boolean> = _scrollEnd

    init {
        fetchImages()
    }

    fun fetchImages() {
        _images.value = UiState.Loading
        viewModelScope.launch {
            _images.value = repository.getData(page, query, type, size)
        }
    }

    fun increasePage(){
        page++
    }

    fun getSpanCount(): Int{
        return spanCount.value ?: DEFAULT_SPAN_COUNT
    }

    fun setSpanCount(spanCount: Int){
        if(spanCount >= SPAN_COUNT_MIN && spanCount <= SPAN_COUNT_MAX)
            _spanCount.value = spanCount
    }

    fun recyclerViewScrollIsEnd(){
        _scrollEnd.value = true
    }

    fun recyclerViewScrollIsNotEnd(){
        _scrollEnd.value = false
    }

}