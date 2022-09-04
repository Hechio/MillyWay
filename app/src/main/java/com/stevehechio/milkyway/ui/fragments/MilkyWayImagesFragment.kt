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
import com.stevehechio.milkyway.data.Resource
import com.stevehechio.milkyway.data.local.entities.MilkyWayEntity
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
        viewModel.getMilkyLiveData().observe(viewLifecycleOwner){response ->
            when(response){
                is Resource.Success ->{
                    mAdapter.submitList(response.data)
                    stopLoadingWithSuccess()
                }
                is Resource.Failure -> {
                    val error = response.cause
                    binding.tvError.text = getString(R.string.woops, error)
                    stopLoadingWithError()
                }
                is Resource.Loading -> {
                    if (mAdapter.itemCount < 1){
                        startLoading()
                    }
                }
                else -> {
                    binding.tvError.text = getString(R.string.something_went_wrong)
                    stopLoadingWithError()
                }
            }
        }
       viewModel.fetchImages()

    }
    private fun stopLoadingWithSuccess() {
        binding.rv.visible()
        binding.pb.gone()
        binding.tvError.gone()
    }
    private fun stopLoadingWithError() {
        binding.rv.gone()
        binding.pb.gone()
        binding.tvError.visible()
    }

    private fun startLoading() {
        binding.rv.gone()
        binding.pb.visible()
        binding.tvError.gone()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear()
        _binding = null
    }

}