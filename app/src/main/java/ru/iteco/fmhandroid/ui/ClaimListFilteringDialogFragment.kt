package ru.iteco.fmhandroid.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.checkbox.MaterialCheckBox
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.dto.Claim
import ru.iteco.fmhandroid.viewmodel.ClaimViewModel

@AndroidEntryPoint
class ClaimListFilteringDialogFragment : DialogFragment() {

    private val viewModel: ClaimViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = requireActivity().layoutInflater.inflate(
            R.layout.claim_filtering_dialog,
            null
        )

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        lifecycleScope.launchWhenStarted {
            viewModel.statusesFlow.collectLatest {
                it.map { status ->
                    displayingCurrentStatusesInCheckboxes(status, dialogView)
                }
            }
        }

        dialog.setOnShowListener {
            val buttonOk: Button =
                dialogView.findViewById(R.id.claim_list_filter_ok_material_button)
            val buttonCancel: Button =
                dialogView.findViewById(R.id.claim_filter_cancel_material_button)
            buttonOk.setOnClickListener {
                val checkedStatusList = mutableListOfClaimStatus(dialogView)

                viewModel.onFilterClaimsMenuItemClicked(checkedStatusList)

                dialog.dismiss()
            }
            buttonCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
        return dialog
    }

    private fun mutableListOfClaimStatus(dialogView: View): MutableList<Claim.Status> {
        val checkedStatusList = mutableListOf<Claim.Status>()

        if (dialogView.findViewById<MaterialCheckBox>(R.id.item_filter_open).isChecked)
            checkedStatusList.add(Claim.Status.OPEN)
        if (dialogView.findViewById<MaterialCheckBox>(R.id.item_filter_in_progress).isChecked)
            checkedStatusList.add(Claim.Status.IN_PROGRESS)
        if (dialogView.findViewById<MaterialCheckBox>(R.id.item_filter_executed).isChecked)
            checkedStatusList.add(Claim.Status.EXECUTED)
        if (dialogView.findViewById<MaterialCheckBox>(R.id.item_filter_cancelled).isChecked)
            checkedStatusList.add(Claim.Status.CANCELLED)
        return checkedStatusList
    }

    private fun displayingCurrentStatusesInCheckboxes(
        status: Claim.Status,
        dialogView: View
    ) {
        fun check(@IdRes id: Int) {
            dialogView.findViewById<MaterialCheckBox>(id).isChecked = true
        }
        when (status) {
            Claim.Status.CANCELLED -> check(R.id.item_filter_cancelled)
            Claim.Status.EXECUTED -> check(R.id.item_filter_executed)
            Claim.Status.IN_PROGRESS -> check(R.id.item_filter_in_progress)
            Claim.Status.OPEN -> check(R.id.item_filter_open)
        }
    }
}
