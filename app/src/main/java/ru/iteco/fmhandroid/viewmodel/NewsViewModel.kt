package ru.iteco.fmhandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.iteco.fmhandroid.adapter.OnNewsItemClickListener
import ru.iteco.fmhandroid.dto.News
import ru.iteco.fmhandroid.dto.User
import ru.iteco.fmhandroid.repository.newsRepository.NewsRepository
import ru.iteco.fmhandroid.repository.userRepository.UserRepository
import ru.iteco.fmhandroid.ui.viewdata.NewsViewData
import ru.iteco.fmhandroid.utils.Utils
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val userRepository: UserRepository
) : ViewModel(), OnNewsItemClickListener {

    private val sortDirection = MutableStateFlow(SortDirection.ASC)
    private val clearFilter = Filter(
        newsCategoryId = null,
        dateStart = null,
        dateEnd = null
    )

    private val filterFlow = MutableStateFlow(clearFilter)
    private val openNewsIds = MutableStateFlow<Set<Int>>(emptySet())
    val loadNewsExceptionEvent = MutableSharedFlow<Unit>()
    val loadNewsCategoriesExceptionEvent = MutableSharedFlow<Unit>()
    val newsListUpdatedEvent = MutableSharedFlow<Unit>()

    val currentUser: User
        get() = userRepository.currentUser

    @ExperimentalCoroutinesApi
    val data: Flow<List<NewsViewData>> by lazy {
        filterFlow.flatMapLatest { filter ->
            newsRepository.getAllNews(
                viewModelScope,
                publishEnabled = true,
                publishDateBefore = Utils.fromLocalDateTimeToTimeStamp(LocalDateTime.now()),
                newsCategoryId = filter.newsCategoryId,
                dateStart = filter.dateStart,
                dateEnd = filter.dateEnd
            ).combine(sortDirection) { news, sortDirection ->
                when (sortDirection) {
                    SortDirection.ASC -> news
                    SortDirection.DESC -> news.reversed()
                }
            }.combine(openNewsIds) { news, openNewsIds ->
                news.map {
                    val newsItem = it.newsItem
                    val id = requireNotNull(newsItem.id) { "News id must not be null" }
                    NewsViewData(
                        id = id,
                        category = it.category,
                        title = newsItem.title,
                        description = newsItem.description,
                        creatorId = it.newsItem.creatorId,
                        creatorName = it.newsItem.creatorName,
                        createDate = newsItem.createDate,
                        publishDate = newsItem.publishDate,
                        publishEnabled = newsItem.publishEnabled,
                        isOpen = openNewsIds.contains(id)
                    )
                }
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            internalOnRefresh()
        }
    }

    private suspend fun internalOnRefresh() {
        try {
            newsRepository.refreshNews()
            newsListUpdatedEvent.emit(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            loadNewsExceptionEvent.emit(Unit)
        }
    }

    fun onSortDirectionButtonClicked() {
        sortDirection.value = sortDirection.value.reverse()
    }

    suspend fun getAllNewsCategories() =
        newsRepository.getAllNewsCategories()
            .catch { e ->
                e.printStackTrace()
                loadNewsCategoriesExceptionEvent.emit(Unit)
            }

    fun onFilterNewsClicked(
        newsCategoryId: Int?,
        dateStart: Long?,
        dateEnd: Long?
    ) {
        filterFlow.value = Filter(
            newsCategoryId = newsCategoryId,
            dateStart = dateStart,
            dateEnd = dateEnd
        )
    }

    fun initializationListNewsCategories(listNewsCategories: List<News.Category>) {
        viewModelScope.launch {
            newsRepository.saveNewsCategories(listNewsCategories)
        }
    }

    enum class SortDirection {
        ASC,
        DESC;

        fun reverse() = when (this) {
            ASC -> DESC
            DESC -> ASC
        }
    }

    private class Filter(
        val newsCategoryId: Int?,
        val dateStart: Long?,
        val dateEnd: Long?
    )

    override fun onCard(newsItem: NewsViewData) {
        if (openNewsIds.value.contains(newsItem.id)) openNewsIds.value -= newsItem.id
        else openNewsIds.value += newsItem.id
    }
}
