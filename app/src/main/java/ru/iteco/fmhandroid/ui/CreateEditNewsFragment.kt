package ru.iteco.fmhandroid.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.databinding.FragmentCreateEditNewsBinding
import ru.iteco.fmhandroid.dto.News
import ru.iteco.fmhandroid.utils.Utils
import ru.iteco.fmhandroid.utils.Utils.convertNewsCategory
import ru.iteco.fmhandroid.utils.Utils.saveDateTime
import ru.iteco.fmhandroid.utils.Utils.updateDateLabel
import ru.iteco.fmhandroid.utils.Utils.updateTimeLabel
import ru.iteco.fmhandroid.viewmodel.NewsControlPanelViewModel
import java.time.Instant.now
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@AndroidEntryPoint
class CreateEditNewsFragment : Fragment(R.layout.fragment_create_edit_news) {
    private val viewModel: NewsControlPanelViewModel by viewModels()
    private val args: CreateEditNewsFragmentArgs by navArgs()

    private lateinit var vPublishDatePicker: TextInputEditText
    private lateinit var vPublishTimePicker: TextInputEditText
    private lateinit var binding: FragmentCreateEditNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        lifecycleScope.launch {
            viewModel.saveNewsItemExceptionEvent.collect {
                showErrorToast(R.string.error_saving)
            }
        }
        lifecycleScope.launch {
            viewModel.editNewsItemExceptionEvent.collect {
                showErrorToast(R.string.error_saving)
            }
        }
        lifecycleScope.launch {
            viewModel.newsItemCreatedEvent.collect {
                findNavController().navigateUp()
            }
        }
        lifecycleScope.launch {
            viewModel.editNewsItemSavedEvent.collect {
                findNavController().navigateUp()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateEditNewsBinding.bind(view)

        with(binding) {
            containerCustomAppBarIncludeOnFragmentCreateEditNews.mainMenuImageButton.visibility =
                View.GONE
            containerCustomAppBarIncludeOnFragmentCreateEditNews.authorizationImageButton.visibility =
                View.GONE
            containerCustomAppBarIncludeOnFragmentCreateEditNews.ourMissionImageButton.visibility =
                View.GONE
            containerCustomAppBarIncludeOnFragmentCreateEditNews.trademarkImageView.visibility =
                View.GONE
            newsItemCategoryTextInputLayout.isStartIconVisible = false
            if (args.newsItemArg == null) {
                containerCustomAppBarIncludeOnFragmentCreateEditNews.customAppBarTitleTextView.apply {
                    visibility = View.VISIBLE
                    setText(R.string.creating)
                    textSize = 18F
                }
                containerCustomAppBarIncludeOnFragmentCreateEditNews.customAppBarSubTitleTextView.apply {
                    visibility = View.VISIBLE
                    setText(R.string.news)
                }
            } else {
                containerCustomAppBarIncludeOnFragmentCreateEditNews.customAppBarTitleTextView.apply {
                    visibility = View.VISIBLE
                    setText(R.string.editing)
                    textSize = 18F
                }
                containerCustomAppBarIncludeOnFragmentCreateEditNews.customAppBarSubTitleTextView.apply {
                    visibility = View.VISIBLE
                    setText(R.string.news)
                }
            }
            args.newsItemArg?.let { newsItem ->
                newsItemCategoryTextAutoCompleteTextView.setText(newsItem.category.name)
                newsItemTitleTextInputEditText.setText(newsItem.newsItem.title)
                newsItemPublishDateTextInputEditText.setText(
                    Utils.formatDate(newsItem.newsItem.publishDate)
                )
                newsItemPublishTimeTextInputEditText.setText(
                    Utils.formatTime(newsItem.newsItem.publishDate)
                )
                newsItemDescriptionTextInputEditText.setText(newsItem.newsItem.description)
                switcher.isChecked = newsItem.newsItem.publishEnabled
            }

            if (args.newsItemArg == null) {
                switcher.isChecked = true
                switcher.isEnabled = false
            }

            if (switcher.isChecked) {
                switcher.setText(R.string.news_item_active)
            } else {
                switcher.setText(R.string.news_item_not_active)
            }

            switcher.setOnClickListener {
                if (switcher.isChecked) {
                    switcher.setText(R.string.news_item_active)
                } else {
                    switcher.setText(R.string.news_item_not_active)
                }
            }

            cancelButton.setOnClickListener {
                val activity = activity ?: return@setOnClickListener
                val dialog = AlertDialog.Builder(activity)
                dialog.setMessage(R.string.cancellation)
                    .setPositiveButton(R.string.fragment_positive_button) { alertDialog, _ ->
                        alertDialog.dismiss()
                        findNavController().navigateUp()
                    }
                    .setNegativeButton(R.string.cancel) { alertDialog, _ ->
                        alertDialog.cancel()
                    }
                    .create()
                    .show()
            }

            saveButton.setOnClickListener {
                if (newsItemCategoryTextAutoCompleteTextView.text.isNullOrBlank() ||
                    newsItemTitleTextInputEditText.text.isNullOrBlank() ||
                    newsItemPublishDateTextInputEditText.text.isNullOrBlank() ||
                    newsItemPublishTimeTextInputEditText.text.isNullOrBlank() ||
                    newsItemDescriptionTextInputEditText.text.isNullOrBlank()
                ) {
                    emptyFieldWarning()
                    showErrorToast(R.string.empty_fields)
                } else {
                    fillNewsItem()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getAllNewsCategories().collect { category ->
                val newsCategoryItems = category.map { it.name }

                with(binding) {
                    val adapter =
                        ArrayAdapter(requireContext(), R.layout.menu_item, newsCategoryItems)
                    newsItemCategoryTextAutoCompleteTextView.setAdapter(adapter)

                    newsItemCategoryTextAutoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
                        val selectedItem = parent.getItemAtPosition(position)
                        val title = binding.newsItemTitleTextInputEditText
                        newsCategoryItems.forEach { category ->
                            if (title.text.isNullOrBlank() || title.text.toString() == category) {
                                title.setText(selectedItem.toString())
                            }
                        }
                    }
                }
            }
        }

        val calendar = Calendar.getInstance()

        vPublishDatePicker = binding.newsItemPublishDateTextInputEditText

        val publishDatePicker =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateLabel(calendar, vPublishDatePicker)
            }

        vPublishDatePicker.setOnClickListener {
            DatePickerDialog(
                this.requireContext(),
                publishDatePicker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                this.datePicker.minDate = (System.currentTimeMillis() - 1000)
            }.show()
        }

        vPublishTimePicker = binding.newsItemPublishTimeTextInputEditText

        val publishTimePicker = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            updateTimeLabel(calendar, vPublishTimePicker)
        }

        vPublishTimePicker.setOnClickListener {
            TimePickerDialog(
                this.requireContext(),
                publishTimePicker,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun FragmentCreateEditNewsBinding.emptyFieldWarning() {
        newsItemCategoryTextInputLayout.isStartIconVisible =
            newsItemCategoryTextAutoCompleteTextView.text.isNullOrBlank()
        if (newsItemTitleTextInputEditText.text.isNullOrBlank()) {
            newsItemTitleTextInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
        } else {
            newsItemTitleTextInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
        }
        if (newsItemPublishDateTextInputEditText.text.isNullOrBlank()) {
            newsItemCreateDateTextInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
        } else {
            newsItemCreateDateTextInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
        }
        if (newsItemPublishTimeTextInputEditText.text.isNullOrBlank()) {
            newsItemPublishTimeTextInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
        } else {
            newsItemPublishTimeTextInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
        }
        if (newsItemDescriptionTextInputEditText.text.isNullOrBlank()) {
            newsItemDescriptionTextInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
        } else {
            newsItemDescriptionTextInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
        }
    }

    private fun showErrorToast(text: Int) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun fillNewsItem() {
        with(binding) {
            val news = args.newsItemArg
            if (news != null) {
                val editedNews = News(
                    id = news.newsItem.id,
                    title = newsItemTitleTextInputEditText.text.toString(),
                    newsCategoryId = convertNewsCategory(
                        newsItemCategoryTextAutoCompleteTextView.text.toString()
                    ),
                    creatorName = news.newsItem.creatorName,
                    createDate = news.newsItem.createDate,
                    creatorId = news.newsItem.creatorId,
                    publishDate = saveDateTime(
                        newsItemPublishDateTextInputEditText.text.toString(),
                        newsItemPublishTimeTextInputEditText.text.toString()
                    ),
                    description = newsItemDescriptionTextInputEditText.text.toString(),
                    publishEnabled = switcher.isChecked
                )
                viewModel.edit(editedNews)
            } else {
                val createdNews = News(
                    id = null,
                    title = newsItemTitleTextInputEditText.text.toString().trim(),
                    newsCategoryId = convertNewsCategory(
                        newsItemCategoryTextAutoCompleteTextView.text.toString()
                    ),
                    creatorName = Utils.fullUserNameGenerator(
                        viewModel.currentUser.lastName,
                        viewModel.currentUser.firstName,
                        viewModel.currentUser.middleName
                    ),
                    createDate = LocalDateTime.now()
                        .toEpochSecond(ZoneId.of("Europe/Moscow").rules.getOffset(now())),
                    creatorId = viewModel.currentUser.id,
                    publishDate = saveDateTime(
                        newsItemPublishDateTextInputEditText.text.toString(),
                        newsItemPublishTimeTextInputEditText.text.toString()
                    ),
                    description = newsItemDescriptionTextInputEditText.text.toString().trim(),
                    publishEnabled = switcher.isChecked
                )
                viewModel.save(createdNews)
            }
        }
    }
}
