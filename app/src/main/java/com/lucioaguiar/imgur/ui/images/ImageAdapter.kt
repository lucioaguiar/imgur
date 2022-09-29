package com.lucioaguiar.imgur.ui.images

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lucioaguiar.imgur.data.models.Image
import com.lucioaguiar.imgur.databinding.ViewHolderImageBinding
import com.lucioaguiar.imgur.util.TypesImagesActions

class ImageAdapter(
    private val onActionClicked: (TypesImagesActions, Image) -> Unit,
) : RecyclerView.Adapter<ImageViewHolder>() {

    private var list = mutableListOf<Image>()

    fun addImageList(imageList: List<Image>){
        this.list.addAll(imageList.toMutableList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderImageBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(list[position], onActionClicked)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}