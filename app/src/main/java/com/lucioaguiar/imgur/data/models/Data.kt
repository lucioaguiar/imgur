package com.lucioaguiar.imgur.data.models

data class Data(
    val data: MutableList<Gallery>,
    val success: Boolean,
    val status: Int
){

    fun getAllImages(): List<Image> {
        val imageList: MutableList<Image> = arrayListOf()
        data.map { gallery ->
            imageList.addAll(gallery.images)
        }
        return imageList
    }

}
