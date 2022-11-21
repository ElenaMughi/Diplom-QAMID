package ru.iteco.fmhandroid.ui

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.adapter.OnOurMissionItemClickListener
import ru.iteco.fmhandroid.adapter.OurMissionItemListAdapter
import ru.iteco.fmhandroid.databinding.FragmentOurMissionBinding
import ru.iteco.fmhandroid.ui.viewdata.OurMissionItemViewData
import ru.iteco.fmhandroid.viewmodel.AuthViewModel
import ru.iteco.fmhandroid.viewmodel.OurMissionViewModel

@AndroidEntryPoint
class OurMissionFragment : Fragment(R.layout.fragment_our_mission) {
    private val viewModel: OurMissionViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentOurMissionBinding.bind(view)

        val mainMenu = PopupMenu(
            context,
            binding.containerCustomAppBarIncludeOnFragmentOurMission.mainMenuImageButton
        )
        mainMenu.inflate(R.menu.menu_main)
        binding.containerCustomAppBarIncludeOnFragmentOurMission.mainMenuImageButton.setOnClickListener {
            mainMenu.show()
        }
        mainMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_main -> {
                    findNavController().navigate(R.id.action_our_mission_fragment_to_mainFragment)
                    true
                }
                R.id.menu_item_claims -> {
                    findNavController().navigate(R.id.action_our_mission_fragment_to_claimListFragment)
                    true
                }
                R.id.menu_item_news -> {
                    findNavController().navigate(R.id.action_our_mission_fragment_to_newsListFragment)
                    true
                }
                R.id.menu_item_about -> {
                    findNavController().navigate(R.id.action_our_mission_fragment_to_aboutFragment)
                    true
                }
                else -> false
            }
        }

        val authorizationMenu = PopupMenu(
            context,
            binding.containerCustomAppBarIncludeOnFragmentOurMission.authorizationImageButton
        )
        authorizationMenu.inflate(R.menu.authorization)

        binding.containerCustomAppBarIncludeOnFragmentOurMission.authorizationImageButton.setOnClickListener {
            authorizationMenu.show()
        }
        authorizationMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.authorization_logout_menu_item -> {
                    authViewModel.logOut()
                    findNavController().navigate(R.id.action_our_mission_fragment_to_authFragment)
                    true
                }
                else -> false
            }
        }

        val adapter = OurMissionItemListAdapter(object : OnOurMissionItemClickListener {
            override fun onCard(ourMissionItem: OurMissionItemViewData) {
                viewModel.onCard(ourMissionItem)
            }
        }, viewModel)

        binding.ourMissionItemListRecyclerView.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest {
                adapter.submitList(it)
            }
        }
    }
}