package ru.iteco.fmhandroid.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.adapter.ClaimListAdapter
import ru.iteco.fmhandroid.adapter.NewsListAdapter
import ru.iteco.fmhandroid.databinding.FragmentMainBinding
import ru.iteco.fmhandroid.utils.Utils
import ru.iteco.fmhandroid.viewmodel.AuthViewModel
import ru.iteco.fmhandroid.viewmodel.ClaimViewModel
import ru.iteco.fmhandroid.viewmodel.NewsViewModel

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding
    private val claimViewModel: ClaimViewModel by viewModels()
    private val newsViewModel: NewsViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        lifecycleScope.launchWhenCreated {
            claimViewModel.onRefresh()
        }

        lifecycleScope.launchWhenStarted {
            claimViewModel.openClaimEvent.collectLatest {
                val action = MainFragmentDirections
                    .actionMainFragmentToOpenClaimFragment(it)
                findNavController().navigate(action)
            }
        }

        lifecycleScope.launchWhenStarted {
            claimViewModel.claimListUpdatedEvent.collectLatest {
                newsViewModel.onRefresh()
            }
        }

        lifecycleScope.launchWhenResumed {
            claimViewModel.claimsLoadException.collect {
                showErrorToast(R.string.error)
            }
        }

        lifecycleScope.launchWhenResumed {
            newsViewModel.loadNewsExceptionEvent.collect {
                showErrorToast(R.string.error)
            }
        }

        lifecycleScope.launchWhenResumed {
            authViewModel.userListLoadedEvent.collect {
                findNavController().navigate(R.id.action_mainFragment_to_createEditClaimFragment)
            }
        }
    }

    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainBinding.bind(view)

        val mainMenu = PopupMenu(
            context,
            binding.containerCustomAppBarIncludeOnFragmentMain.mainMenuImageButton
        )
        mainMenu.inflate(R.menu.menu_main)
        val menuItemMain = mainMenu.menu.getItem(0)
        menuItemMain.isEnabled = false
        binding.containerCustomAppBarIncludeOnFragmentMain.mainMenuImageButton.setOnClickListener {
            mainMenu.show()
        }
        mainMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_claims -> {
                    findNavController().navigate(R.id.action_mainFragment_to_claimListFragment)
                    true
                }
                R.id.menu_item_news -> {
                    findNavController().navigate(R.id.action_mainFragment_to_newsListFragment)
                    true
                }
                R.id.menu_item_about -> {
                    findNavController().navigate(R.id.action_mainFragment_to_aboutFragment)
                    true
                }
                else -> false
            }
        }

        val authorizationMenu = PopupMenu(
            context,
            binding.containerCustomAppBarIncludeOnFragmentMain.authorizationImageButton
        )
        authorizationMenu.inflate(R.menu.authorization)

        binding.containerCustomAppBarIncludeOnFragmentMain.authorizationImageButton.setOnClickListener {
            authorizationMenu.show()
        }

        binding.containerCustomAppBarIncludeOnFragmentMain.ourMissionImageButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_our_mission_fragment)
        }

        authorizationMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.authorization_logout_menu_item -> {
                    authViewModel.logOut()
                    findNavController().navigate(R.id.action_mainFragment_to_authFragment)
                    true
                }
                else -> false
            }
        }

        binding.containerListClaimIncludeOnFragmentMain.apply {
            expandMaterialButton.visibility = View.VISIBLE
            allClaimsTextView.visibility = View.VISIBLE
            filtersMaterialButton.visibility = View.GONE

            addNewClaimMaterialButton.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    authViewModel.loadUserList()
                }
            }

            expandMaterialButton.setOnClickListener {
                when (allClaimsTextView.visibility) {
                    View.GONE -> {
                        allClaimsTextView.visibility = View.VISIBLE
                        allClaimsCardsBlockConstraintLayout.visibility = View.VISIBLE
                        expandMaterialButton.setIconResource(R.drawable.expand_less_24)
                    }
                    else -> {
                        allClaimsTextView.visibility = View.GONE
                        allClaimsCardsBlockConstraintLayout.visibility = View.GONE
                        expandMaterialButton.setIconResource(R.drawable.expand_more_24)
                    }
                }
            }

            allClaimsTextView.setOnClickListener {
                if (Utils.isOnline(requireContext())) {
                    findNavController().navigate(R.id.action_mainFragment_to_claimListFragment)
                } else {
                    showErrorToast(R.string.error)
                }
            }
        }

        val claimListAdapter = ClaimListAdapter(claimViewModel)

        binding.containerListClaimIncludeOnFragmentMain.claimListRecyclerView.adapter =
            claimListAdapter
        lifecycleScope.launchWhenCreated {
            claimViewModel.data.collectLatest { state ->
                claimListAdapter.submitList(state.take(n = 6))
            }
        }

        binding.containerListNewsIncludeOnFragmentMain.apply {
            sortNewsMaterialButton.visibility = View.GONE
            filterNewsMaterialButton.visibility = View.GONE
            editNewsMaterialButton.visibility = View.GONE

            expandMaterialButton.setOnClickListener {
                when (allNewsTextView.visibility) {
                    View.GONE -> {
                        allNewsTextView.visibility = View.VISIBLE
                        allNewsCardsBlockConstraintLayout.visibility = View.VISIBLE
                        expandMaterialButton.setIconResource(R.drawable.expand_less_24)
                    }
                    else -> {
                        allNewsTextView.visibility = View.GONE
                        allNewsCardsBlockConstraintLayout.visibility = View.GONE
                        expandMaterialButton.setIconResource(R.drawable.expand_more_24)
                    }
                }
            }

            allNewsTextView.setOnClickListener {
                if (Utils.isOnline(requireContext())) {
                    findNavController().navigate(R.id.action_mainFragment_to_newsListFragment)
                } else {
                    showErrorToast(R.string.error)
                }
            }
        }

        val newsListAdapter = NewsListAdapter(newsViewModel)
        binding.containerListNewsIncludeOnFragmentMain.newsListRecyclerView.adapter =
            newsListAdapter
        lifecycleScope.launchWhenCreated {
            newsViewModel.data.collectLatest {
                newsListAdapter.submitList(it.take(3))
            }
        }

        lifecycleScope.launch {
            binding.mainSwipeRefresh.setOnRefreshListener {
                claimViewModel.onRefresh()
                binding.mainSwipeRefresh.isRefreshing = false
            }
        }
    }

    private fun showErrorToast(text: Int) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_LONG
        ).show()
    }
}
