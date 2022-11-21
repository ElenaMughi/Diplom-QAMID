package ru.iteco.fmhandroid.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.databinding.FragmentFilterNewsBinding
import ru.iteco.fmhandroid.dto.NewsFilterArgs
import ru.iteco.fmhandroid.enum.FragmentsTags
import ru.iteco.fmhandroid.utils.Utils.saveDateTime
import ru.iteco.fmhandroid.utils.Utils.updateDateLabel
import ru.iteco.fmhandroid.viewmodel.NewsViewModel
import java.util.*

@AndroidEntryPoint
class FilterNewsListFragment : Fragment(R.layout.fragment_filter_news) {
    private lateinit var binding: FragmentFilterNewsBinding
    private lateinit var vPublishDateStartPicker: TextInputEditText
    private lateinit var vPublishDateEndPicker: TextInputEditText
    private val newsListViewModel: NewsViewModel by viewModels()

    private val nameFragment: FragmentsTags by lazy {
        val args by navArgs<FilterNewsListFragmentArgs>()
        args.fragmentName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilterNewsBinding.bind(view)

        when (nameFragment) {
            FragmentsTags.NEWS_LIST_FRAGMENT -> {
                binding.filterNewsActiveMaterialCheckBox.visibility = View.GONE
                binding.filterNewsInactiveMaterialCheckBox.visibility = View.GONE
            }
            FragmentsTags.NEWS_CONTROL_PANEL_FRAGMENT -> {
                binding.filterNewsActiveMaterialCheckBox.visibility = View.VISIBLE
                binding.filterNewsInactiveMaterialCheckBox.visibility = View.VISIBLE
                binding.filterNewsActiveMaterialCheckBox.isChecked = true
                binding.filterNewsInactiveMaterialCheckBox.isChecked = true
            }
        }

        lifecycleScope.launch {
            newsListViewModel.getAllNewsCategories().collect { category ->
                val newsCategoryItems = category.map { it.name }

                val adapter = ArrayAdapter(requireContext(), R.layout.menu_item, newsCategoryItems)
                binding.newsItemCategoryTextAutoCompleteTextView.setAdapter(adapter)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            newsListViewModel.loadNewsCategoriesExceptionEvent.collect {
                val activity = activity ?: return@collect
                val dialog = android.app.AlertDialog.Builder(activity)
                dialog.setMessage(R.string.error)
                    .setPositiveButton(R.string.fragment_positive_button) { alertDialog, _ ->
                        alertDialog.cancel()
                    }
                    .create()
                    .show()
            }
        }

        val calendar = Calendar.getInstance()

        vPublishDateStartPicker = binding.newsItemPublishDateStartTextInputEditText

        val publishDateStartPicker =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateLabel(calendar, vPublishDateStartPicker)
            }

        vPublishDateStartPicker.setOnClickListener {
            DatePickerDialog(
                this.requireContext(),
                publishDateStartPicker,
                calendar.get(Calendar.YEAR),
                calendar.get(
                    Calendar.MONTH
                ),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        vPublishDateEndPicker = binding.newsItemPublishDateEndTextInputEditText

        val publishDateEndPicker =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateLabel(calendar, vPublishDateEndPicker)
            }

        vPublishDateEndPicker.setOnClickListener {
            DatePickerDialog(
                this.requireContext(),
                publishDateEndPicker,
                calendar.get(Calendar.YEAR),
                calendar.get(
                    Calendar.MONTH
                ),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        var category: String? = null
        binding.newsItemCategoryTextAutoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            category = if (position >= 0) parent.getItemAtPosition(position).toString() else null
        }

        var dates: List<Long>? = null
        var status: Boolean?

        binding.filterButton.setOnClickListener {
            status =
                if (binding.filterNewsActiveMaterialCheckBox.isChecked && !binding.filterNewsInactiveMaterialCheckBox.isChecked) {
                    true
                } else if (!binding.filterNewsActiveMaterialCheckBox.isChecked && binding.filterNewsInactiveMaterialCheckBox.isChecked) {
                    false
                } else {
                    null
                }

            if (vPublishDateStartPicker.text.toString().isNotBlank() &&
                vPublishDateEndPicker.text.toString().isNotBlank()
            ) {
                dates = listOf(
                    saveDateTime(vPublishDateStartPicker.text.toString(), "00:00"),
                    saveDateTime(vPublishDateEndPicker.text.toString(), "23:59")
                )
                navigateUp(category, dates, status)

            } else if (vPublishDateStartPicker.text.toString().isNotBlank() &&
                vPublishDateEndPicker.text.isNullOrBlank() ||
                vPublishDateStartPicker.text.isNullOrBlank() &&
                vPublishDateEndPicker.text.toString().isNotBlank()
            ) {
                val activity = activity ?: return@setOnClickListener
                val dialog = android.app.AlertDialog.Builder(activity)
                dialog.setMessage(R.string.wrong_news_date_period)
                    .setPositiveButton(R.string.fragment_positive_button) { alertDialog, _ ->
                        alertDialog.cancel()
                    }
                    .create()
                    .show()

            } else navigateUp(category, dates, status)
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navigateUp(category: String?, dates: List<Long>?, status: Boolean?) {
        val newsFilterArgs = NewsFilterArgs(
            category = category,
            dates = dates,
            status = status
        )
        setFragmentResult("requestKey", bundleOf("filterArgs" to newsFilterArgs))
        findNavController().navigateUp()
    }
}
