package ru.iteco.fmhandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.databinding.ItemCommentBinding
import ru.iteco.fmhandroid.dto.ClaimComment
import ru.iteco.fmhandroid.utils.Utils
import ru.iteco.fmhandroid.viewmodel.ClaimCardViewModel

interface OnClaimCommentItemClickListener {
    fun onCard(claimComment: ClaimComment)
}

class ClaimCommentListAdapter(
    private val onClaimCommentItemClickListener: OnClaimCommentItemClickListener,
    private val claimCardViewModel: ClaimCardViewModel
) : ListAdapter<ClaimComment, ClaimCommentListAdapter.ClaimCommentViewHolder>(
    ClaimCommentDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimCommentViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ClaimCommentViewHolder(binding, onClaimCommentItemClickListener, claimCardViewModel)
    }

    override fun onBindViewHolder(holder: ClaimCommentViewHolder, position: Int) {
        val claimComment = getItem(position)
        holder.bind(claimComment)
    }

    class ClaimCommentViewHolder(
        private val binding: ItemCommentBinding,
        private val onClaimCommentItemClickListener: OnClaimCommentItemClickListener,
        private val claimCardViewModel: ClaimCardViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(claimComment: ClaimComment) {
            with(binding) {
                commentDescriptionTextView.text = claimComment.description
                commentatorNameTextView.text = claimComment.creatorName

                commentDateTextView.text =
                    Utils.formatDate(claimComment.createDate)
                commentTimeTextView.text =
                    Utils.formatTime(claimComment.createDate)

                editCommentImageButton.setImageResource(
                    if (claimComment.creatorId != claimCardViewModel.currentUser.id) R.drawable.ic_pen_light else R.drawable.ic_pen
                )
                editCommentImageButton.setOnClickListener {
                    if (claimCardViewModel.currentUser.id == claimComment.creatorId) {
                        onClaimCommentItemClickListener.onCard(claimComment)
                    } else {
                        return@setOnClickListener
                    }
                }
            }
        }
    }

    private object ClaimCommentDiffCallback : DiffUtil.ItemCallback<ClaimComment>() {
        override fun areItemsTheSame(
            oldItem: ClaimComment,
            newItem: ClaimComment
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ClaimComment,
            newItem: ClaimComment
        ): Boolean {
            return oldItem == newItem
        }
    }
}
