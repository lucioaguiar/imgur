package com.lucioaguiar.imgur.ui.images

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lucioaguiar.imgur.data.models.Image
import com.lucioaguiar.imgur.databinding.ViewHolderImageBinding
import com.lucioaguiar.imgur.util.TypesImagesActions

class ImageViewHolder(private val binding: ViewHolderImageBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(image: Image, onActionClicked: (TypesImagesActions, Image) -> Unit){

        Glide.with(binding.root)
            .load(image.link)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.ivImage)

        itemView.setOnClickListener {
            onActionClicked(TypesImagesActions.ITEM, image)
        }

    }
}