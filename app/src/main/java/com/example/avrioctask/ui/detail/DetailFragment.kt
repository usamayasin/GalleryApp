package com.example.avrioctask.ui.detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.avrioctask.R
import com.example.avrioctask.adapters.MediaAdapter
import com.example.avrioctask.data.model.ContentState
import com.example.avrioctask.data.model.ErrorState
import com.example.avrioctask.data.model.LoadingState
import com.example.avrioctask.data.model.UIState
import com.example.avrioctask.databinding.DetailFragmentBinding
import com.example.avrioctask.ui.MainViewModel
import com.example.avrioctask.utils.Constants
import com.example.avrioctask.utils.gone
import com.example.avrioctask.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var binding: DetailFragmentBinding
    private lateinit var mediaAdapter: MediaAdapter
    private var albumName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        binding.mediaItemsRv.layoutManager = GridLayoutManager(requireContext(), 2)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.albumName = requireArguments().getString(Constants.BUNDLE_KEY)
        this.albumName?.let {
            viewModel.fetchMediaItems(it)
        }
        mediaAdapter = MediaAdapter(
            onImageClick = ::onImageClick,
            onVideoClick = ::onVideoClick
        )
        binding.mediaItemsRv.adapter = mediaAdapter

        initObservers()

    }

    private fun initObservers() {
        mainViewModel.isGridLayoutManager.observe(viewLifecycleOwner) { isGridLayout ->
            if (isGridLayout) {
                binding.mediaItemsRv.layoutManager = GridLayoutManager(requireContext(), 2)
            } else {
                binding.mediaItemsRv.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        viewModel.mediaLiveData.observe(viewLifecycleOwner) { response ->
            response?.let {
                mediaAdapter.differ.submitList(response)
            }
        }

        val uiStateObserver = Observer<UIState> { uiState ->
            when (uiState) {
                is LoadingState -> {
                    binding.errorMessage.gone()
                    binding.progressbar.visible()
                }

                is ContentState -> {
                    binding.errorMessage.gone()
                    binding.progressbar.gone()
                }

                is ErrorState -> {
                    binding.errorMessage.text = uiState.message
                    binding.errorMessage.visible()
                    binding.progressbar.gone()
                }
            }
        }
        viewModel.uiStateLiveData.observe(viewLifecycleOwner, uiStateObserver)

    }


    private fun onImageClick(uri: Uri) {
        val bundle = bundleOf(Constants.ARG_URI to uri.toString())
        findNavController().navigate(
            R.id.toImageFragment,
            bundle
        )
    }

    private fun onVideoClick(uri: Uri) {
        val bundle = bundleOf(Constants.ARG_URI to uri.toString())
        findNavController().navigate(
            R.id.toVideoFragment,
            bundle
        )
    }
}