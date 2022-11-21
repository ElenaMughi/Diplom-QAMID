package ru.iteco.fmhandroid.ui

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.iteco.fmhandroid.BuildConfig
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.databinding.FragmentAboutBinding

class AboutFragment : Fragment(R.layout.fragment_about) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAboutBinding.bind(view)
        binding.aboutVersionValueTextView.text = BuildConfig.VERSION_NAME
        binding.aboutPrivacyPolicyValueTextView.movementMethod = LinkMovementMethod.getInstance()
        with(binding.containerCustomAppBarIncludeOnFragmentAbout) {
            ourMissionImageButton.visibility = View.GONE
            authorizationImageButton.visibility = View.GONE
            mainMenuImageButton.visibility = View.GONE
            aboutBackImageButton.visibility = View.VISIBLE
        }
        binding.containerCustomAppBarIncludeOnFragmentAbout.aboutBackImageButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
