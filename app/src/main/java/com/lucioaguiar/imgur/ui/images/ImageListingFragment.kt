package com.lucioaguiar.imgur.ui.images

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lucioaguiar.imgur.R
import com.lucioaguiar.imgur.data.models.Image
import com.lucioaguiar.imgur.databinding.FragmentImageListingBinding
import com.lucioaguiar.imgur.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImageListingFragment : Fragment() {

    private val viewModel: ImageListingViewModel by viewModel()
    lateinit var imageAdapter: ImageAdapter
    lateinit var binding: FragmentImageListingBinding
    lateinit var mScaleGestureDetector: ScaleGestureDetector

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageListingBinding.inflate(layoutInflater)
        context ?: return binding.root

        mScaleGestureDetector = ScaleGestureDetector(requireContext(),
            GestureDetectorImpl(viewModel.getSpanCount()) { spanCount -> changeSpanCount(
                spanCount
            )
        })

        imageAdapter = ImageAdapter { action, image -> click(
                action, image
            )}

        binding.rvImageList.apply {
            layoutManager = GridLayoutManager(context, viewModel.getSpanCount())
            adapter = imageAdapter
        }

        subscribeUi()
        setupRecyclerViewScrollListener()
        setupRecyclerViewTouchListener()

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupRecyclerViewTouchListener() {
        binding.rvImageList.setOnTouchListener { v, event ->
            mScaleGestureDetector.onTouchEvent(event)
            false
        }
    }

    private fun changeSpanCount(spanCount: Int) {
        viewModel.setSpanCount(spanCount)
    }

    private fun setupRecyclerViewScrollListener() {
        binding.rvImageList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!canScrollDown(recyclerView)) {
                    viewModel.recyclerViewScrollIsEnd()
                } else {
                    viewModel.recyclerViewScrollIsNotEnd()
                }
            }
        })
    }

    private fun canScrollDown(recyclerView: RecyclerView): Boolean {
        return recyclerView.canScrollVertically(1)
    }

    private fun click(action: TypesImagesActions, image: Image) {
        when (action) {
            TypesImagesActions.ITEM -> {
                toast(getString(R.string.imageLink, image.link))
            }
        }
    }

    private fun subscribeUi() {
        viewModel.scrollEnd.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.fetchImages()
            }
        }

        viewModel.spanCount.observe(viewLifecycleOwner) { spanCount ->
            binding.rvImageList.setLayoutManager(GridLayoutManager(context, spanCount))
        }

        viewModel.images.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.pbLoadMore.show()
                }
                is UiState.Failure -> {
                    toast(state.error)
                    binding.pbLoadMore.hide()
                }
                is UiState.Success -> {
                    state.data.let { list ->
                        binding.pbLoadMore.hide()
                        imageAdapter.addImageList(list)
                        viewModel.increasePage()
                    }
                }
            }
        }

    }

}