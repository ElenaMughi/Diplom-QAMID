package ru.iteco.fmhandroid.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.adapter.NewsListAdapter
import ru.iteco.fmhandroid.databinding.FragmentNewsListBinding
import ru.iteco.fmhandroid.dto.NewsFilterArgs
import ru.iteco.fmhandroid.enum.FragmentsTags
import ru.iteco.fmhandroid.utils.Utils.convertNewsCategory
import ru.iteco.fmhandroid.viewmodel.AuthViewModel
import ru.iteco.fmhandroid.viewmodel.NewsViewModel

@AndroidEntryPoint
class NewsListFragment : Fragment(R.layout.fragment_news_list) {
    private lateinit var binding: FragmentNewsListBinding
    private val viewModel: NewsViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        lifecycleScope.launchWhenCreated {
            viewModel.onRefresh()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsListBinding.bind(view)
        val mainMenu = PopupMenu(
            context,
            binding.containerCustomAppBarIncludeOnFragmentNewsList.mainMenuImageButton
        )
        mainMenu.inflate(R.menu.menu_main)
        val menuItemNews = mainMenu.menu.getItem(2)
        menuItemNews.isEnabled = false

        mainMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_main -> {
                    findNavController().navigate(R.id.action_newsListFragment_to_mainFragment)
                    true
                }
                R.id.menu_item_claims -> {
                    findNavController().navigate(R.id.action_newsListFragment_to_claimListFragment)
                    true
                }
                R.id.menu_item_about -> {
                    findNavController().navigate(R.id.action_newsListFragment_to_aboutFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }

        binding.containerCustomAppBarIncludeOnFragmentNewsList.ourMissionImageButton.setOnClickListener {
            findNavController().navigate(R.id.action_newsListFragment_to_our_mission_fragment)
        }

        val authorizationMenu = PopupMenu(
            context,
            binding.containerCustomAppBarIncludeOnFragmentNewsList.authorizationImageButton
        )
        authorizationMenu.inflate(R.menu.authorization)

        binding.containerCustomAppBarIncludeOnFragmentNewsList.authorizationImageButton.setOnClickListener {
            authorizationMenu.show()
        }

        authorizationMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.authorization_logout_menu_item -> {
                    authViewModel.logOut()
                    findNavController().navigate(R.id.action_newsListFragment_to_authFragment)
                    true
                }
                else -> false
            }
        }

        binding.apply {
            containerListNewsInclude.allNewsTextView.visibility = View.GONE
            containerListNewsInclude.expandMaterialButton.visibility = View.GONE
        }

        val adapter = NewsListAdapter(viewModel)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.data.collectLatest {
                binding.newsListSwipeRefresh.isRefreshing = false
                adapter.submitList(it)
                if (it.isEmpty()) {
                    binding.containerListNewsInclude.emptyNewsListGroup.isVisible = true
                    binding.containerListNewsInclude.newsRetryMaterialButton.setOnClickListener {
                        binding.newsListSwipeRefresh.isRefreshing = true
                        viewModel.onRefresh()
                        binding.newsListSwipeRefresh.isRefreshing = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.loadNewsExceptionEvent.collect {
                val activity = activity ?: return@collect
                val dialog = AlertDialog.Builder(activity)
                dialog.setMessage(R.string.error)
                    .setPositiveButton(R.string.fragment_positive_button) { alertDialog, _ ->
                        alertDialog.cancel()
                    }
                    .create()
                    .show()
            }
        }

        binding.newsListSwipeRefresh.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.onRefresh()
                delay(200)
                binding.containerListNewsInclude.newsListRecyclerView.scrollToPosition(
                    0
                )
            }
        }

        with(binding) {
            containerListNewsInclude.editNewsMaterialButton.setOnClickListener {
                if (viewModel.currentUser.admin) {
                    findNavController().navigate(
                        R.id.action_newsListFragment_to_newsControlPanelFragment
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.no_rules_for_news_control_panel,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            containerListNewsInclude.sortNewsMaterialButton.setOnClickListener {
                viewModel.onSortDirectionButtonClicked()
            }

            containerCustomAppBarIncludeOnFragmentNewsList.mainMenuImageButton.setOnClickListener {
                mainMenu.show()
            }

            containerListNewsInclude.filterNewsMaterialButton.setOnClickListener {
                val action = NewsListFragmentDirections.actionNewsListFragmentToFilterNewsFragment(
                    FragmentsTags.NEWS_LIST_FRAGMENT
                )
                findNavController().navigate(action)
            }
        }

        binding.containerListNewsInclude.newsListRecyclerView.adapter = adapter

        setFragmentResultListener("requestKey") { _, bundle ->
            val args = bundle.getParcelable<NewsFilterArgs>("filterArgs")
            viewModel.onFilterNewsClicked(
                args?.category?.let { convertNewsCategory(it) },
                args?.dates?.get(0),
                args?.dates?.get(1)
            )
        }
    }
}
