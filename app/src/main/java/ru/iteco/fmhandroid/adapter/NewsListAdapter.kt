package ru.iteco.fmhandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.databinding.ItemNewsBinding
import ru.iteco.fmhandroid.dto.News
import ru.iteco.fmhandroid.extensions.getType
import ru.iteco.fmhandroid.ui.viewdata.NewsViewData
import ru.iteco.fmhandroid.utils.Utils

interface OnNewsItemClickListener {
    fun onCard(newsItem: NewsViewData)
}

class NewsListAdapter(private val onNewsItemClickListener: OnNewsItemClickListener) :
    ListAdapter<NewsViewData, NewsListAdapter.NewsViewHolder>(NewsDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding, onNewsItemClickListener)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsViewData = getItem(position)
        holder.bind(newsViewData)
    }

    class NewsViewHolder(
        private val binding: ItemNewsBinding,
        private val onNewsItemClickListener: OnNewsItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsViewData) {
            with(binding) {
                newsItemTitleTextView.text = news.title
                newsItemDescriptionTextView.text = news.description
                newsItemDateTextView.text =
                    Utils.formatDate(news.publishDate)

                setCategoryIcon(news)

                if (news.isOpen) {
                    newsItemGroup.visibility = View.VISIBLE
                    viewNewsItemImageView.setImageResource(R.drawable.expand_less_24)
                } else {
                    newsItemGroup.visibility = View.GONE
                    viewNewsItemImageView.setImageResource(R.drawable.expand_more_24)
                }

                newsItemMaterialCardView.setOnClickListener {
                    onNewsItemClickListener.onCard(news)
                }
            }
        }

        private fun setCategoryIcon(news: NewsViewData) {
            val iconResId = when (news.category.getType()) {
                News.Category.Type.Advertisement -> R.raw.icon_advertisement
                News.Category.Type.Salary -> R.raw.icon_salary
                News.Category.Type.Union -> R.raw.icon_union
                News.Category.Type.Birthday -> R.raw.icon_birthday
                News.Category.Type.Holiday -> R.raw.icon_holiday
                News.Category.Type.Massage -> R.raw.icon_massage
                News.Category.Type.Gratitude -> R.raw.icon_gratitude
                News.Category.Type.Help -> R.raw.icon_help
                News.Category.Type.Unknown -> return
            }
            binding.categoryIconImageView.setImageResource(iconResId)
        }
    }
}

private object NewsDiffCallBack : DiffUtil.ItemCallback<NewsViewData>() {
    override fun areItemsTheSame(oldItem: NewsViewData, newItem: NewsViewData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NewsViewData, newItem: NewsViewData): Boolean {
        return oldItem == newItem
    }
}
