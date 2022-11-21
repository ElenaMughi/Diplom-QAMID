package ru.iteco.fmhandroid.ui

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.adapter.ClaimListAdapter
import ru.iteco.fmhandroid.databinding.FragmentListClaimBinding
import ru.iteco.fmhandroid.viewmodel.AuthViewModel
import ru.iteco.fmhandroid.viewmodel.ClaimViewModel

@AndroidEntryPoint
class ClaimListFragment : Fragment(R.layout.fragment_list_claim) {
    private lateinit var binding: FragmentListClaimBinding
    private val viewModel: ClaimViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.onRefresh()
        }

        lifecycleScope.launchWhenCreated {
            authViewModel.getUserListExceptionEvent.collectLatest {
                Toast.makeText(
                    requireContext(),
                    R.string.error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.claimsLoadException.collect {
                Toast.makeText(
                    requireContext(),
                    R.string.error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        lifecycleScope.launch {
            viewModel.openClaimEvent.collectLatest {
                val action = ClaimListFragmentDirections
                    .actionClaimListFragmentToOpenClaimFragment(it)
                findNavController().navigate(action)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.claimCommentsLoadExceptionEvent.collectLatest {
                Toast.makeText(
                    requireContext(),
                    R.string.claim_comments_load_error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        lifecycleScope.launchWhenResumed {
            authViewModel.userListLoadedEvent.collect {
                findNavController().navigate(R.id.action_claimListFragment_to_createEditClaimFragment)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListClaimBinding.bind(view)

        val mainMenu = PopupMenu(
            context,
            binding.containerCustomAppBarIncludeOnFragmentListClaim.mainMenuImageButton
        )
        mainMenu.inflate(R.menu.menu_main)
        val menuItemClaims = mainMenu.menu.getItem(1)
        menuItemClaims.isEnabled = false

        mainMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_main -> {
                    findNavController().navigate(R.id.action_claimListFragment_to_mainFragment)
                    true
                }
                R.id.menu_item_news -> {
                    findNavController().navigate(R.id.action_claimListFragment_to_newsListFragment)
                    true
                }
                R.id.menu_item_about -> {
                    findNavController().navigate(R.id.action_claimListFragment_to_aboutFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }

        binding.containerCustomAppBarIncludeOnFragmentListClaim.ourMissionImageButton.setOnClickListener {
            findNavController().navigate(R.id.action_claimListFragment_to_our_mission_fragment)
        }

        val authorizationMenu = PopupMenu(
            context,
            binding.containerCustomAppBarIncludeOnFragmentListClaim.authorizationImageButton
        )
        authorizationMenu.inflate(R.menu.authorization)

        binding.containerCustomAppBarIncludeOnFragmentListClaim.authorizationImageButton.setOnClickListener {
            authorizationMenu.show()
        }

        authorizationMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.authorization_logout_menu_item -> {
                    authViewModel.logOut()
                    findNavController().navigate(R.id.action_claimListFragment_to_authFragment)
                    true
                }
                else -> false
            }
        }

        val adapter = ClaimListAdapter(viewModel)

        binding.claimListSwipeRefresh.setOnRefreshListener {
            viewModel.onRefresh()
            binding.claimListSwipeRefresh.isRefreshing = false
        }

        binding.containerListClaimInclude.claimListRecyclerView.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.data.collectLatest { state ->
                adapter.submitList(state)

                delay(200)
                binding.containerListClaimInclude.claimListRecyclerView.smoothScrollToPosition(0)

                if (state.isEmpty()) {
                    binding.containerListClaimInclude.emptyClaimListGroup.isVisible = true
                    binding.containerListClaimInclude.claimRetryMaterialButton.setOnClickListener {
                        binding.claimListSwipeRefresh.isRefreshing = true
                        viewModel.onRefresh()
                        binding.claimListSwipeRefresh.isRefreshing = false
                    }
                } else {
                    binding.containerListClaimInclude.emptyClaimListGroup.isVisible = false
                }
            }
        }

        binding.containerListClaimInclude.filtersMaterialButton.setOnClickListener {
            val dialog = ClaimListFilteringDialogFragment()
            dialog.show(childFragmentManager, "custom")
        }

        binding.containerCustomAppBarIncludeOnFragmentListClaim.mainMenuImageButton.setOnClickListener {
            mainMenu.show()
        }

        binding.containerListClaimInclude.addNewClaimMaterialButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                authViewModel.loadUserList()
            }
        }
    }
}
