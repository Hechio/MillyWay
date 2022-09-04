package com.stevehechio.milkyway.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.stevehechio.milkyway.R
import com.stevehechio.milkyway.data.local.entities.MilkyWayEntity
import com.stevehechio.milkyway.databinding.ItemMickyWayBinding
import com.stevehechio.milkyway.utils.gone
import com.stevehechio.milkyway.utils.toDate
import java.util.*

class MilkyWayAdapter(val context: Context):
    ListAdapter<MilkyWayEntity, MilkyWayAdapter.MilkyWayViewHolder>(MILKY_WAY_COMPARATOR){
    private var lastPosition = -1

    var onClickItemListener: OnClickItemListener? = null

    fun setOnClickedItem(onClickItemListener : OnClickItemListener){
        this.onClickItemListener = onClickItemListener
    }
    override fun onBindViewHolder(holder: MilkyWayViewHolder, position: Int) {
        getItem(position)?.let { holder.bindViews(it) }
        setAnimation(holder.itemView, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilkyWayViewHolder {
        return MilkyWayViewHolder(ItemMickyWayBinding
            .inflate(LayoutInflater.from(parent.context),parent,false))
    }
    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val anim = ScaleAnimation(
                0.0f,
                1.0f,
                0.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            anim.duration =
                Random().nextInt(501).toLong() //to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim)
            lastPosition = position
        }
    }
    inner class MilkyWayViewHolder(val binding: ItemMickyWayBinding)
        : RecyclerView.ViewHolder(binding.root){
            fun bindViews(milkyWayEntity: MilkyWayEntity){
                val mUrl = milkyWayEntity.links.first().imageUrl
                Glide.with(context)
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
                    .into(binding.ivMilkyWay)
                binding.tvName.text = milkyWayEntity.milkyWayData.first().title
                val center = milkyWayEntity.milkyWayData.first().center
                val date = milkyWayEntity.milkyWayData.first().date_created.toDate()
                binding.tvCenterDate.text = context.getString(R.string.slash,center, date)
                binding.root.setOnClickListener { onClickItemListener?.onItemClicked(
                    milkyWayEntity, binding.ivMilkyWay,binding.tvName) }
            }
        }
    companion object {
        private val MILKY_WAY_COMPARATOR = object : DiffUtil.ItemCallback<MilkyWayEntity>(){
            override fun areItemsTheSame(
                oldItem: MilkyWayEntity,
                newItem: MilkyWayEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MilkyWayEntity,
                newItem: MilkyWayEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface OnClickItemListener{
        fun onItemClicked(milkyWayEntity: MilkyWayEntity,imageview: ImageView, textView: TextView)
    }
}