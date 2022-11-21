package ru.iteco.fmhandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.iteco.fmhandroid.databinding.ItemClaimBinding
import ru.iteco.fmhandroid.dto.FullClaim
import ru.iteco.fmhandroid.utils.Utils

interface OnClaimItemClickListener {
    fun onCard(fullClaim: FullClaim) {}
}

class ClaimListAdapter(
    private val onClaimItemClickListener: OnClaimItemClickListener
) : ListAdapter<FullClaim, ClaimListAdapter.ClaimViewHolder>(
    ClaimDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimViewHolder {
        val binding = ItemClaimBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ClaimViewHolder(binding, onClaimItemClickListener)
    }

    override fun onBindViewHolder(holder: ClaimViewHolder, position: Int) {
        val fullClaim = getItem(position)
        holder.bind(fullClaim)
    }

    class ClaimViewHolder(
        private val binding: ItemClaimBinding,
        private val onClaimItemClickListener: OnClaimItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(fullClaim: FullClaim) {
            with(binding) {
                executorNameMaterialTextView.text = fullClaim.claim.executorName
                planTimeMaterialTextView.text =
                    Utils.formatTime(
                        fullClaim.claim.planExecuteDate
                    )

                planDateMaterialTextView.text =
                    Utils.formatDate(
                        fullClaim.claim.planExecuteDate
                    )

                descriptionMaterialTextView.text = fullClaim.claim.title

                claimListCard.setOnClickListener {
                    onClaimItemClickListener.onCard(fullClaim)
                }
            }
        }
    }

    private object ClaimDiffCallback : DiffUtil.ItemCallback<FullClaim>() {
        override fun areItemsTheSame(
            oldItem: FullClaim,
            newItem: FullClaim
        ): Boolean {
            return oldItem.claim.id == newItem.claim.id
        }

        override fun areContentsTheSame(
            oldItem: FullClaim,
            newItem: FullClaim
        ): Boolean {
            return oldItem == newItem
        }
    }
}
