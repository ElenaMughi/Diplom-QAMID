package ru.iteco.fmhandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.iteco.fmhandroid.dto.News
import ru.iteco.fmhandroid.dto.NewsWithCategory
import ru.iteco.fmhandroid.dto.User
import ru.iteco.fmhandroid.repository.newsRepository.NewsRepository
import ru.iteco.fmhandroid.repository.userRepository.UserRepository
import javax.inject.Inject

@HiltViewModel
class NewsControlPanelViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val clearFilter = Filter(
        newsCategoryId = null,
        dateStart = null,
        dateEnd = null,
        status = null
    )
    private val sortDirection = MutableStateFlow(NewsViewModel.SortDirection.ASC)
    private val filterFlow = MutableStateFlow(
        clearFilter
    )

    val currentUser: User
        get() = userRepository.currentUser

    val loadNewsExceptionEvent = MutableSharedFlow<Unit>()
    val newsItemCreatedEvent = MutableSharedFlow<Unit>()
    val saveNewsItemExceptionEvent = MutableSharedFlow<Unit>()
    val editNewsItemSavedEvent = MutableSharedFlow<Unit>()
    val editNewsItemExceptionEvent = MutableSharedFlow<Unit>()
    val removeNewsItemExceptionEvent = MutableSharedFlow<Unit>()

    @FlowPreview
    val data: Flow<List<NewsWithCategory>> = filterFlow.flatMapMerge { filter ->
        newsRepository.getAllNews(
            viewModelScope,
            newsCategoryId = filter.newsCategoryId,
            dateStart = filter.dateStart,
            dateEnd = filter.dateEnd,
            status = filter.status
        ).combine(sortDirection) { news, sortDirection ->
            when (sortDirection) {
                NewsViewModel.SortDirection.ASC -> news
                NewsViewModel.SortDirection.DESC -> news.reversed()
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
        } catch (e: Exception) {
            e.printStackTrace()
            loadNewsExceptionEvent.emit(Unit)
        }
    }

    fun onSortDirectionButtonClicked() {
        sortDirection.value = sortDirection.value.reverse()
    }

    fun onFilterNewsClicked(
        newsCategoryId: Int?,
        dateStart: Long?,
        dateEnd: Long?,
        status: Boolean?
    ) {
        filterFlow.value = Filter(
            newsCategoryId = newsCategoryId,
            dateStart = dateStart,
            dateEnd = dateEnd,
            status = status
        )
    }

    fun save(newsItem: News) {
        viewModelScope.launch {
            try {
                newsRepository.createNews(newsItem)
                newsItemCreatedEvent.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                saveNewsItemExceptionEvent.emit(Unit)
            }
        }
    }

    fun edit(newsItem: News) {
        viewModelScope.launch {
            try {
                newsRepository.modificationOfExistingNews(newsItem)
                editNewsItemSavedEvent.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                editNewsItemExceptionEvent.emit(Unit)
            }
        }
    }

    fun remove(id: Int) {
        viewModelScope.launch {
            try {
                newsRepository.removeNewsItemById(id)
            } catch (e: Exception) {
                e.printStackTrace()
                removeNewsItemExceptionEvent.emit(Unit)
            }
        }
    }

    fun getAllNewsCategories() = newsRepository.getAllNewsCategories()

    private class Filter(
        val newsCategoryId: Int?,
        val dateStart: Long?,
        val dateEnd: Long?,
        val status: Boolean?
    )

    fun onCard(newsItem: News) {
        viewModelScope.launch {
            if (newsItem.isOpen) {
                newsRepository.changeIsOpen(newsItem.copy(isOpen = false))
            } else {
                newsRepository.changeIsOpen(newsItem.copy(isOpen = true))
            }
        }
    }
}
