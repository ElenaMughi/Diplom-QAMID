package ru.iteco.fmhandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.databinding.ItemOurMissionBinding
import ru.iteco.fmhandroid.ui.viewdata.OurMissionItemViewData
import ru.iteco.fmhandroid.viewmodel.OurMissionViewModel

interface OnOurMissionItemClickListener {
    fun onCard(ourMissionItem: OurMissionItemViewData)
}

class OurMissionItemListAdapter(
    private val onOurMissionItemClickListener: OnOurMissionItemClickListener,
    val viewModel: OurMissionViewModel
) : ListAdapter<OurMissionItemViewData, OurMissionItemListAdapter.OurMissionViewHolder>(
    OurMissionDiffCallback
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OurMissionViewHolder {
        val binding = ItemOurMissionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OurMissionViewHolder(
            binding,
            onOurMissionItemClickListener,
            viewModel
        )
    }

    override fun onBindViewHolder(
        holder: OurMissionViewHolder,
        position: Int
    ) {
        val ourMissionItem = getItem(position)
        holder.bind(ourMissionItem)
    }

    class OurMissionViewHolder(
        val binding: ItemOurMissionBinding,
        val onOurMissionItemClickListener: OnOurMissionItemClickListener,
        val viewModel: OurMissionViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ourMissionItem: OurMissionItemViewData) {
            with(binding) {
                ourMissionItemTitleTextView.text = ourMissionItem.title
                ourMissionItemTitleTextView.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        ourMissionItem.titleBackgroundColor
                    )
                )
                ourMissionItemDescriptionTextView.text = ourMissionItem.description

                if (ourMissionItem.isOpen) {
                    ourMissionItemDescriptionTextView.visibility = View.VISIBLE
                    ourMissionItemOpenCardImageButton.setImageResource(R.drawable.expand_less_24)
                } else {
                    ourMissionItemDescriptionTextView.visibility = View.GONE
                    ourMissionItemOpenCardImageButton.setImageResource(R.drawable.expand_more_24)
                }

                ourMissionItemMaterialCardView.setOnClickListener {
                    onOurMissionItemClickListener.onCard(ourMissionItem)
                }
            }
        }
    }

    private object OurMissionDiffCallback : DiffUtil.ItemCallback<OurMissionItemViewData>() {
        override fun areItemsTheSame(
            oldItem: OurMissionItemViewData,
            newItem: OurMissionItemViewData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: OurMissionItemViewData,
            newItem: OurMissionItemViewData
        ): Boolean {
            return oldItem == newItem
        }
    }
}