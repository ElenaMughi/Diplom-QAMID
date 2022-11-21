package ru.iteco.fmhandroid.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
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
import ru.iteco.fmhandroid.databinding.FragmentCreateEditClaimBinding
import ru.iteco.fmhandroid.dto.Claim
import ru.iteco.fmhandroid.dto.User
import ru.iteco.fmhandroid.utils.Utils
import ru.iteco.fmhandroid.utils.Utils.fullUserNameGenerator
import ru.iteco.fmhandroid.utils.Utils.saveDateTime
import ru.iteco.fmhandroid.utils.Utils.updateDateLabel
import ru.iteco.fmhandroid.utils.Utils.updateTimeLabel
import ru.iteco.fmhandroid.viewmodel.ClaimCardViewModel
import java.time.LocalDateTime
import java.util.*

@AndroidEntryPoint
class CreateEditClaimFragment : Fragment(R.layout.fragment_create_edit_claim) {
    private lateinit var vDatePicker: TextInputEditText
    private lateinit var vTimePicker: TextInputEditText
    private lateinit var binding: FragmentCreateEditClaimBinding
    private val args: CreateEditClaimFragmentArgs by navArgs()
    private var executor: User? = null
    private val claimCardViewModel: ClaimCardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        lifecycleScope.launch {
            claimCardViewModel.createClaimExceptionEvent.collect {
                showErrorToast(R.string.error)
            }
        }
        lifecycleScope.launch {
            claimCardViewModel.claimUpdateExceptionEvent.collect {
                showErrorToast(R.string.error)
            }
        }
        lifecycleScope.launch {
            claimCardViewModel.claimCreatedEvent.collect {
                findNavController().navigateUp()
            }
        }
        lifecycleScope.launch {
            claimCardViewModel.claimUpdatedEvent.collect {
                findNavController().navigateUp()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateEditClaimBinding.bind(view)

        with(binding) {
            containerCustomAppBarIncludeOnFragmentCreateEditClaim.mainMenuImageButton.visibility =
                View.GONE
            containerCustomAppBarIncludeOnFragmentCreateEditClaim.authorizationImageButton.visibility =
                View.GONE
            containerCustomAppBarIncludeOnFragmentCreateEditClaim.ourMissionImageButton.visibility =
                View.GONE
            containerCustomAppBarIncludeOnFragmentCreateEditClaim.trademarkImageView.visibility =
                View.GONE
            if (args.argClaim == null) {
                containerCustomAppBarIncludeOnFragmentCreateEditClaim.customAppBarTitleTextView.apply {
                    visibility = View.VISIBLE
                    setText(R.string.creating)
                    textSize = 18F
                }
                containerCustomAppBarIncludeOnFragmentCreateEditClaim.customAppBarSubTitleTextView.apply {
                    visibility = View.VISIBLE
                    setText(R.string.claims)
                }
            } else {
                containerCustomAppBarIncludeOnFragmentCreateEditClaim.customAppBarTitleTextView.apply {
                    visibility = View.VISIBLE
                    setText(R.string.editing)
                    textSize = 18F
                }
                containerCustomAppBarIncludeOnFragmentCreateEditClaim.customAppBarSubTitleTextView.apply {
                    visibility = View.VISIBLE
                    setText(R.string.claims)
                }
            }

            args.argClaim?.let { claim ->
                titleTextInputLayout.editText?.setText(claim.claim.title)
                dateInPlanTextInputLayout.editText?.setText(
                    Utils.formatDate(claim.claim.planExecuteDate)
                )
                timeInPlanTextInputLayout.editText?.setText(
                    Utils.formatTime(claim.claim.planExecuteDate)
                )
                descriptionTextInputLayout.editText?.setText(claim.claim.description)
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
                val activity = activity ?: return@setOnClickListener
                val dialog = AlertDialog.Builder(activity)
                if (titleTextInputLayout.editText?.text.isNullOrBlank() ||
                    dateInPlanTextInputLayout.editText?.text.isNullOrBlank() ||
                    timeInPlanTextInputLayout.editText?.text.isNullOrBlank() ||
                    descriptionTextInputLayout.editText?.text.isNullOrBlank()
                ) {
                    emptyFieldWarning()

                    dialog.setMessage(R.string.empty_fields)
                        .setPositiveButton(R.string.fragment_positive_button) { alertDialog, _ ->
                            alertDialog.cancel()
                        }
                        .create()
                        .show()
                } else {
                    fillClaim()
                }
            }
        }

        lifecycleScope.launch {

            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.menu_item,
                claimCardViewModel.userList.map { user ->
                    fullUserNameGenerator(
                        user.lastName,
                        user.firstName,
                        user.middleName
                    )
                })

            binding.executorDropMenuAutoCompleteTextView.apply {
                setAdapter(adapter)
                setOnItemClickListener { _, _, position, _ ->
                    executor = claimCardViewModel.userList[position]
                }
            }
        }

        val myCalendar = Calendar.getInstance()
        vDatePicker = binding.dateInPlanTextInputEditText
        vTimePicker = binding.timeInPlanTextInputEditText

        /* DatePickerDialog */
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel(myCalendar, vDatePicker)
        }

        vDatePicker.setOnClickListener {
            DatePickerDialog(
                this.requireContext(),
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                this.datePicker.minDate = (System.currentTimeMillis() - 1000)
            }.show()
        }

        /* TimePickerDialog */
        val timePicker = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            myCalendar.set(Calendar.HOUR_OF_DAY, hour)
            myCalendar.set(Calendar.MINUTE, minute)
            updateTimeLabel(myCalendar, vTimePicker)
        }

        vTimePicker.setOnClickListener {
            TimePickerDialog(
                this.requireContext(),
                timePicker,
                myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun FragmentCreateEditClaimBinding.emptyFieldWarning() {
        if (titleTextInputLayout.editText?.text.isNullOrBlank()) {
            titleTextInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
        } else {
            titleTextInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
        }
        if (dateInPlanTextInputLayout.editText?.text.isNullOrBlank()) {
            dateInPlanTextInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
        } else {
            dateInPlanTextInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
        }
        if (timeInPlanTextInputLayout.editText?.text.isNullOrBlank()) {
            timeInPlanTextInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
        } else {
            timeInPlanTextInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
        }
        if (descriptionTextInputLayout.editText?.text.isNullOrBlank()) {
            descriptionTextInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
        } else {
            descriptionTextInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
        }
    }

    private fun fillClaim() {
        with(binding) {
            val fullClaim = args.argClaim
            if (fullClaim != null) {
                val editedClaim = Claim(
                    id = fullClaim.claim.id,
                    title = titleEditText.text.toString(),
                    description = descriptionEditText.text.toString(),
                    executorId = executor?.id,
                    creatorName = fullClaim.claim.creatorName,
                    createDate = fullClaim.claim.createDate,
                    creatorId = fullClaim.claim.creatorId,
                    planExecuteDate = saveDateTime(
                        dateInPlanTextInputEditText.text.toString(),
                        timeInPlanTextInputEditText.text.toString()
                    ),
                    status = fullClaim.claim.status
                )
                claimCardViewModel.updateClaim(editedClaim)
            } else {
                val createdClaim = Claim(
                    id = null,
                    title = titleEditText.text.toString().trim(),
                    description = descriptionEditText.text.toString().trim(),
                    executorId = executor?.id,
                    creatorName = fullUserNameGenerator(
                        lastName = claimCardViewModel.currentUser.lastName,
                        firstName = claimCardViewModel.currentUser.firstName,
                        middleName = claimCardViewModel.currentUser.middleName
                    ),
                    createDate = Utils.fromLocalDateTimeToTimeStamp(
                        LocalDateTime.now()
                    ),
                    creatorId = claimCardViewModel.currentUser.id,
                    planExecuteDate = saveDateTime(
                        dateInPlanTextInputEditText.text.toString(),
                        timeInPlanTextInputEditText.text.toString()
                    ),
                    status = Claim.Status.OPEN
                )
                claimCardViewModel.save(createdClaim)
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
