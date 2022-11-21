package ru.iteco.fmhandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.adapter.ClaimCommentListAdapter
import ru.iteco.fmhandroid.adapter.OnClaimCommentItemClickListener
import ru.iteco.fmhandroid.databinding.FragmentOpenClaimBinding
import ru.iteco.fmhandroid.dto.Claim
import ru.iteco.fmhandroid.dto.ClaimComment
import ru.iteco.fmhandroid.dto.FullClaim
import ru.iteco.fmhandroid.utils.Utils
import ru.iteco.fmhandroid.viewmodel.AuthViewModel
import ru.iteco.fmhandroid.viewmodel.ClaimCardViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@AndroidEntryPoint
class OpenClaimFragment : Fragment() {
    private lateinit var binding: FragmentOpenClaimBinding
    private val claimCardViewModel: ClaimCardViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    val claimId: Int by lazy {
        val args by navArgs<OpenClaimFragmentArgs>()
        args.argClaim.claim.id!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        claimCardViewModel.init(claimId)

        lifecycleScope.launchWhenResumed {
            claimCardViewModel.openClaimCommentEvent.collect {
                val action = OpenClaimFragmentDirections
                    .actionOpenClaimFragmentToCreateEditClaimCommentFragment(
                        it,
                        it.claimId
                    )
                findNavController().navigate(action)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launchWhenResumed {
            claimCardViewModel.claimStatusChangeExceptionEvent.collect {
                showErrorToast(R.string.error)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_open_claim, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOpenClaimBinding.bind(view)

        binding.containerCustomAppBarIncludeOnFragmentOpenClaim.customAppBarTitleTextView.visibility =
            View.GONE

        binding.containerCustomAppBarIncludeOnFragmentOpenClaim.customAppBarSubTitleTextView
            .setText(R.string.claim)

        val mainMenu = PopupMenu(
            context,
            binding.containerCustomAppBarIncludeOnFragmentOpenClaim.mainMenuImageButton
        )
        mainMenu.inflate(R.menu.menu_main)
        binding.containerCustomAppBarIncludeOnFragmentOpenClaim.mainMenuImageButton.setOnClickListener {
            mainMenu.show()
        }
        mainMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_main -> {
                    findNavController().navigate(R.id.action_openClaimFragment_to_mainFragment)
                    true
                }
                R.id.menu_item_claims -> {
                    findNavController().navigate(R.id.action_openClaimFragment_to_claimListFragment)
                    true
                }
                R.id.menu_item_news -> {
                    findNavController().navigate(R.id.action_openClaimFragment_to_newsListFragment)
                    true
                }
                R.id.menu_item_about -> {
                    findNavController().navigate(R.id.action_openClaimFragment_to_aboutFragment)
                    true
                }
                else -> false
            }
        }

        binding.containerCustomAppBarIncludeOnFragmentOpenClaim.ourMissionImageButton.setOnClickListener {
            findNavController().navigate(R.id.action_openClaimFragment_to_our_mission_fragment)
        }

        val authorizationMenu = PopupMenu(
            context,
            binding.containerCustomAppBarIncludeOnFragmentOpenClaim.authorizationImageButton
        )
        authorizationMenu.inflate(R.menu.authorization)

        binding.containerCustomAppBarIncludeOnFragmentOpenClaim.authorizationImageButton.setOnClickListener {
            authorizationMenu.show()
        }

        authorizationMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.authorization_logout_menu_item -> {
                    authViewModel.logOut()
                    findNavController().navigate(R.id.action_openClaimFragment_to_authFragment)
                    true
                }
                else -> false
            }
        }

        val adapter = ClaimCommentListAdapter(object : OnClaimCommentItemClickListener {
            override fun onCard(claimComment: ClaimComment) {
                claimCardViewModel.onCard(claimComment)
            }
        }, claimCardViewModel)

        binding.claimCommentsListRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            claimCardViewModel.dataFullClaim.collect { fullClaim ->
                renderingContentOfClaim(fullClaim)
                adapter.submitList(fullClaim.comments)
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

    private fun displayingStatusOfClaim(claimStatus: Claim.Status) =
        when (claimStatus) {
            Claim.Status.CANCELLED -> getString(R.string.status_claim_canceled)
            Claim.Status.EXECUTED -> getString(R.string.executed)
            Claim.Status.IN_PROGRESS -> getString(R.string.in_progress)
            Claim.Status.OPEN -> getString(R.string.status_open)
        }

    private fun statusMenuVisibility(
        claimStatus: Claim.Status,
        statusProcessingMenu: PopupMenu
    ) {
        when (claimStatus) {
            Claim.Status.OPEN -> {
                statusProcessingMenu.menu.setGroupVisible(R.id.open_menu_group, true)
                statusProcessingMenu.menu.setGroupVisible(
                    R.id.in_progress_menu_group,
                    false
                )
            }
            Claim.Status.IN_PROGRESS -> {
                statusProcessingMenu.menu.setGroupVisible(R.id.open_menu_group, false)
                statusProcessingMenu.menu.setGroupVisible(
                    R.id.in_progress_menu_group,
                    true
                )
            }
            else -> {
                statusProcessingMenu.menu.clear()
                binding.statusProcessingImageButton.apply {
                    setImageResource(R.drawable.ic_status_processing_non_clickable)
                }
            }
        }
    }

    private fun renderingContentOfClaim(fullClaim: FullClaim) {
        val statusProcessingMenu = PopupMenu(context, binding.statusProcessingImageButton)
        statusProcessingMenu.inflate(R.menu.menu_claim_status_processing)
        binding.titleTextView.text = fullClaim.claim.title
        binding.planeDateTextView.text = Utils.formatDate(fullClaim.claim.planExecuteDate)
        binding.planTimeTextView.text = Utils.formatTime(fullClaim.claim.planExecuteDate)
        binding.descriptionTextView.text = fullClaim.claim.description
        binding.authorNameTextView.text = fullClaim.claim.creatorName
        binding.createDataTextView.text = Utils.formatDate(fullClaim.claim.createDate)
        binding.createTimeTextView.text = Utils.formatTime(fullClaim.claim.createDate)
        binding.statusLabelTextView.text = displayingStatusOfClaim(fullClaim.claim.status)

        statusMenuVisibility(
            fullClaim.claim.status,
            statusProcessingMenu
        )

        binding.executorNameTextView.text =
            fullClaim.claim.executorName ?: getString(R.string.not_assigned)

        binding.editProcessingImageButton.apply {
            if (
                (fullClaim.claim.status == Claim.Status.OPEN && fullClaim.claim.creatorId == claimCardViewModel.currentUser.id) ||
                (fullClaim.claim.status == Claim.Status.OPEN && claimCardViewModel.currentUser.admin)
            ) {
                this.setImageResource(R.drawable.ic_pen)
                this.isClickable = true
                this.setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        authViewModel.userListLoadedEvent.collectLatest {
                            val action = OpenClaimFragmentDirections
                                .actionOpenClaimFragmentToCreateEditClaimFragment(fullClaim)
                            findNavController().navigate(action)
                        }
                    }
                    viewLifecycleOwner.lifecycleScope.launch {
                        authViewModel.loadUserList()
                    }
                }
            } else {
                this.setImageResource(R.drawable.ic_pen_light)
                this.isClickable = false
                this.setOnClickListener {
                    showErrorToast(R.string.inability_to_edit_claim)
                }
            }
        }

        when (fullClaim.claim.status) {
            Claim.Status.OPEN -> {
                statusProcessingMenu.menu.findItem(R.id.cancel_list_item).isEnabled =
                    claimCardViewModel.currentUser.id == fullClaim.claim.creatorId
            }
            Claim.Status.IN_PROGRESS -> {
                if (claimCardViewModel.currentUser.id != fullClaim.claim.executorId) {
                    binding.statusProcessingImageButton.setImageResource(R.drawable.ic_status_processing_non_clickable)
                    statusProcessingMenu.menu.clear()
                }
            }
            Claim.Status.CANCELLED -> {
            }
            else -> returnTransition
        }

        binding.closeImageButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.statusProcessingImageButton.setOnClickListener {
            statusProcessingMenu.show()
        }

        binding.addCommentImageButton.setOnClickListener {
            val action = OpenClaimFragmentDirections
                .actionOpenClaimFragmentToCreateEditClaimCommentFragment(
                    argComment = null,
                    argClaimId = fullClaim.claim.id!!
                )
            findNavController().navigate(action)
        }

        statusProcessingMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.in_progress_list_item -> {
                    claimCardViewModel.changeClaimStatus(
                        claimId = fullClaim.claim.id!!,
                        newClaimStatus = Claim.Status.IN_PROGRESS,
                        executorId = claimCardViewModel.currentUser.id,
                        claimComment = Utils.Empty.emptyClaimComment
                    )
                    true
                }

                R.id.cancel_list_item -> {
                    claimCardViewModel.changeClaimStatus(
                        fullClaim.claim.id!!,
                        Claim.Status.CANCELLED,
                        executorId = null,
                        claimComment = Utils.Empty.emptyClaimComment
                    )
                    true
                }

                R.id.throw_off_list_item -> {
                    val view = requireActivity().layoutInflater.inflate(
                        R.layout.fragment_dialog_comment_create,
                        null
                    )

                    val dialog = AlertDialog.Builder(requireContext())
                        .setView(view)
                        .setPositiveButton(android.R.string.ok, null)
                        .setNegativeButton(android.R.string.cancel, null)
                        .create()

                    dialog.setOnShowListener {
                        val buttonOk: Button =
                            (dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                        val buttonCancel: Button =
                            (dialog).getButton(AlertDialog.BUTTON_NEGATIVE)
                        buttonOk.setOnClickListener {
                            val editText = view.findViewById<EditText>(R.id.editText)
                            val text = editText.text
                            if (text.isBlank()) {
                                showErrorToast(R.string.toast_empty_field)
                            } else {
                                claimCardViewModel.changeClaimStatus(
                                    fullClaim.claim.id!!,
                                    Claim.Status.OPEN,
                                    executorId = null,
                                    claimComment = ClaimComment(
                                        claimId = fullClaim.claim.id,
                                        creatorName = Utils.fullUserNameGenerator(
                                            claimCardViewModel.currentUser.lastName,
                                            claimCardViewModel.currentUser.firstName,
                                            claimCardViewModel.currentUser.middleName
                                        ),
                                        description = text.toString(),
                                        creatorId = claimCardViewModel.currentUser.id,
                                        createDate = LocalDateTime.now()
                                            .toEpochSecond(
                                                ZoneId.of("Europe/Moscow").rules.getOffset(
                                                    Instant.now()
                                                )
                                            )
                                    )
                                )
                                dialog.dismiss()
                            }
                        }
                        buttonCancel.setOnClickListener {
                            dialog.dismiss()
                        }
                    }
                    dialog.show()
                    true
                }

                R.id.executes_list_item -> {
                    val view = requireActivity().layoutInflater.inflate(
                        R.layout.fragment_dialog_comment_create,
                        null
                    )

                    val dialog = AlertDialog.Builder(requireContext())
                        .setView(view)
                        .setPositiveButton(android.R.string.ok, null)
                        .setNegativeButton(android.R.string.cancel, null)
                        .create()

                    dialog.setOnShowListener {
                        val buttonOk: Button =
                            (dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                        val buttonCancel: Button =
                            (dialog).getButton(AlertDialog.BUTTON_NEGATIVE)
                        buttonOk.setOnClickListener {
                            val editText = view.findViewById<EditText>(R.id.editText)
                            val text = editText.text
                            if (text.isBlank()) {
                                showErrorToast(R.string.toast_empty_field)
                            } else {
                                claimCardViewModel.changeClaimStatus(
                                    fullClaim.claim.id!!,
                                    Claim.Status.EXECUTED,
                                    executorId = fullClaim.claim.executorId,
                                    claimComment = ClaimComment(
                                        claimId = fullClaim.claim.id,
                                        creatorName = Utils.fullUserNameGenerator(
                                            claimCardViewModel.currentUser.lastName,
                                            claimCardViewModel.currentUser.firstName,
                                            claimCardViewModel.currentUser.middleName
                                        ),
                                        description = text.toString(),
                                        creatorId = claimCardViewModel.currentUser.id,
                                        createDate = LocalDateTime.now().toEpochSecond(
                                            ZoneId.of("Europe/Moscow").rules.getOffset(
                                                Instant.now()
                                            )
                                        )
                                    )
                                )
                                dialog.dismiss()
                            }
                        }
                        buttonCancel.setOnClickListener {
                            dialog.dismiss()
                        }
                    }
                    dialog.show()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}
