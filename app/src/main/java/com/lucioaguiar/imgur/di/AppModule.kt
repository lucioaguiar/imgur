package com.lucioaguiar.imgur.di

import com.lucioaguiar.imgur.data.repositories.GalleryRepository
import com.lucioaguiar.imgur.data.repositories.GalleryRepositoryImpl
import com.lucioaguiar.imgur.ui.images.ImageListingViewModel
import com.lucioaguiar.imgur.rest.RetrofitService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RetrofitService.getInstance() }
    single<GalleryRepository> { GalleryRepositoryImpl(get()) }
    viewModel { ImageListingViewModel(get()) }
}