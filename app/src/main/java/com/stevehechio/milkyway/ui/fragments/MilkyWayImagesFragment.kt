package com.stevehechio.milkyway.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.stevehechio.milkyway.R
import com.stevehechio.milkyway.local.entities.MilkyWayEntity
import com.stevehechio.milkyway.databinding.FragmentMilkyWayImagesBinding
import com.stevehechio.milkyway.ui.adapters.MilkyWayAdapter
import com.stevehechio.milkyway.ui.viewmodel.MilkyWayViewModel
import com.stevehechio.milkyway.utils.gone
import com.stevehechio.milkyway.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MilkyWayImagesFragment : Fragment() {
    private var _binding: FragmentMilkyWayImagesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MilkyWayViewModel by activityViewModels()
    private lateinit var mAdapter: MilkyWayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentMilkyWayImagesBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        mAdapter = MilkyWayAdapter(requireContext())
        mAdapter.setOnClickedItem(object : MilkyWayAdapter.OnClickItemListener{
            override fun onItemClicked(
                milkyWayEntity: MilkyWayEntity,
                imageview: ImageView,
                textView: TextView
            ) {
                val extras = FragmentNavigatorExtras(
                    imageview to "milkyImage",
                    textView to "milkyTitle"
                )
                val bundle = Bundle()
                bundle.putSerializable(ImageDescriptionFragment.MILKY_ENTITY,milkyWayEntity)
                findNavController().navigate(R.id.imageDescriptionFragment, bundle,null, extras)
            }

        })
        binding.rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
        fetchMilkyImages()
    }

    private fun fetchMilkyImages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchImages().collect{ pagingData ->
                mAdapter.submitData(pagingData)
                Log.d("MilkyFragment", "fetch images count: ${mAdapter.itemCount}")
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            mAdapter.loadStateFlow.collect{ loadState ->
                binding.rv.isVisible =  loadState.source.refresh is LoadState.NotLoading ||
                        loadState.mediator?.refresh is LoadState.NotLoading
                binding.pb.isVisible = loadState.mediator?.refresh is LoadState.Loading

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    binding.apply {
                        tvError.visible()
                        pb.gone()
                        rv.gone()
                        tvError.text = getString(R.string.woops, it.error.localizedMessage)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear()
        _binding = null
    }

}