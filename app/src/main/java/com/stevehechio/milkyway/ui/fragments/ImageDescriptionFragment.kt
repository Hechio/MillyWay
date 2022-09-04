package com.stevehechio.milkyway.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.stevehechio.milkyway.data.local.entities.MilkyWayEntity
import com.stevehechio.milkyway.databinding.FragmentImageDescriptionBinding
import com.stevehechio.milkyway.utils.gone
import com.stevehechio.milkyway.utils.toDate


class ImageDescriptionFragment : Fragment() {
    private var _binding: FragmentImageDescriptionBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater
            .from(requireContext()).inflateTransition(android.R.transition.move)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageDescriptionBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val milkyWayEntity = it.getSerializable(MILKY_ENTITY) as MilkyWayEntity
            val mUrl = milkyWayEntity.links.first().imageUrl
            Glide.with(requireContext())
                .load(mUrl)
                .centerCrop()
                .dontAnimate()
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.pb.gone()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.pb.gone()
                        return false
                    }
                })
                .into(binding.ivMilkyWayDesc)
            binding.apply {
                tvName.text = milkyWayEntity.milkyWayData.first().title
                tvCenter.text = milkyWayEntity.milkyWayData.first().center
                tvDate.text = milkyWayEntity.milkyWayData.first().date_created.toDate()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvDescription.text = Html.fromHtml(milkyWayEntity.milkyWayData.first().description, 0)
                }else{
                    tvDescription.text = Html.fromHtml(milkyWayEntity.milkyWayData.first().description)
                }
                cvBack.setOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
       const val MILKY_ENTITY = "ImageDescriptionFragment.MilkyEntity"
    }
}