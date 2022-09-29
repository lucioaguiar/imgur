package com.lucioaguiar.imgur.data.repositories

import com.lucioaguiar.imgur.data.models.Image
import com.lucioaguiar.imgur.util.UiState

interface GalleryRepository {
    suspend fun getData(page: Int, query: String, type: String, size: String): UiState<List<Image>>
}