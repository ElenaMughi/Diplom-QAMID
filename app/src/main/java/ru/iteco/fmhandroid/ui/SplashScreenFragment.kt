package ru.iteco.fmhandroid.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.api.UserApi
import ru.iteco.fmhandroid.auth.AppAuth
import ru.iteco.fmhandroid.databinding.FragmentSplashScreenBinding
import ru.iteco.fmhandroid.dto.SplashScreenData
import ru.iteco.fmhandroid.viewmodel.AuthViewModel
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    @Inject
    lateinit var auth: AppAuth

    @Inject
    lateinit var userApi: UserApi
    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var binding: FragmentSplashScreenBinding

    private val splashscreenImages = listOf(
        SplashScreenData(
            R.drawable.image_splashscreen_1,
            "Любая помощь важна и нужна",
            R.drawable.background_splash_screen_title_1,
            R.color.splash_screen_title_color_1
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_2,
            "Бережное отношение к пациентам и их близким",
            R.drawable.background_splash_screen_title_2,
            R.color.splash_screen_title_color_2
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_3,
            "Творческий и осознанный подход к жизни до конца",
            R.drawable.background_splash_screen_title_2,
            R.color.splash_screen_title_color_2
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_4,
            "Ответственно и осознанно нести добро людям",
            R.drawable.background_splash_screen_title_4,
            R.color.splash_screen_title_color_4
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_5,
            "Помощь – это создание комфорта для пациентов и их близких",
            R.drawable.background_splash_screen_title_5,
            R.color.splash_screen_title_color_5
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_6,
            "Ответственно и осознанно нести добро людям",
            R.drawable.background_splash_screen_title_1,
            R.color.splash_screen_title_color_1
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_7,
            "Творческий и осознанный подход к жизни пациента",
            R.drawable.background_splash_screen_title_6,
            R.color.splash_screen_title_color_6
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_8,
            "Добро есть везде и во всех",
            R.drawable.background_splash_screen_title_4,
            R.color.splash_screen_title_color_4
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_9,
            "Ответственная доброта",
            R.drawable.background_splash_screen_title_2,
            R.color.splash_screen_title_color_2
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_10,
            "Создание физического и психологического пространства для завершения жизни",
            R.drawable.background_splash_screen_title_3,
            R.color.splash_screen_title_color_3
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_11,
            "Творческий и осознанный подход к жизни пациента",
            R.drawable.background_splash_screen_title_6,
            R.color.splash_screen_title_color_6
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_12,
            "Чем больше мы принимаем добра, тем больше отдаем",
            R.drawable.background_splash_screen_title_1,
            R.color.splash_screen_title_color_1
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_13,
            "Хоспис – это воплощенная гуманность",
            R.drawable.background_splash_screen_title_5,
            R.color.splash_screen_title_color_5
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_14,
            "Хоспис — это призвание и служение человечеству",
            R.drawable.background_splash_screen_title_6,
            R.color.splash_screen_title_color_6
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_16,
            "Ответственно и осознанно нести добро людям",
            R.drawable.background_splash_screen_title_3,
            R.color.splash_screen_title_color_3
        ),
        SplashScreenData(
            R.drawable.image_splashscreen_17,
            "Хоспис – это компетентная помощь и любовь к пациентам",
            R.drawable.background_splash_screen_title_4,
            R.color.splash_screen_title_color_4
        )
    )
    private val splashscreenImage = splashscreenImages.random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onFullScreen()

        lifecycleScope.launch {
            authViewModel.nonAuthorizedEvent.collectLatest {
                val action = SplashScreenFragmentDirections
                    .actionSplashScreenFragmentToAuthFragment()
                findNavController().navigate(action)
            }
        }

        lifecycleScope.launch {
            authViewModel.authorizedEvent.collectLatest {
                findNavController().navigate(R.id.action_splashScreenFragment_to_mainFragment)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSplashScreenBinding.bind(view)

        when (splashscreenImage.titleBackground) {
            R.drawable.background_splash_screen_title_1 -> {
                binding.splashScreenCircularProgressIndicator.setIndicatorColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.splash_screen_title_1_progress_bar
                    )
                )
                binding.splashScreenCircularProgressIndicator.trackColor =
                    ContextCompat.getColor(requireContext(), R.color.splash_screen_title_1)
            }
            R.drawable.background_splash_screen_title_2 -> {
                binding.splashScreenCircularProgressIndicator.setIndicatorColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.splash_screen_title_2_progress_bar
                    )
                )
                binding.splashScreenCircularProgressIndicator.trackColor =
                    ContextCompat.getColor(requireContext(), R.color.splash_screen_title_2)
            }
            R.drawable.background_splash_screen_title_3 -> {
                binding.splashScreenCircularProgressIndicator.setIndicatorColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.splash_screen_title_3_progress_bar
                    )
                )
                binding.splashScreenCircularProgressIndicator.trackColor =
                    ContextCompat.getColor(requireContext(), R.color.splash_screen_title_3)
            }
            R.drawable.background_splash_screen_title_4 -> {
                binding.splashScreenCircularProgressIndicator.setIndicatorColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.splash_screen_title_4_progress_bar
                    )
                )
                binding.splashScreenCircularProgressIndicator.trackColor =
                    ContextCompat.getColor(requireContext(), R.color.splash_screen_title_4)
            }
            R.drawable.background_splash_screen_title_5 -> {
                binding.splashScreenCircularProgressIndicator.setIndicatorColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.splash_screen_title_5_progress_bar
                    )
                )
                binding.splashScreenCircularProgressIndicator.trackColor =
                    ContextCompat.getColor(requireContext(), R.color.splash_screen_title_5)
            }
            R.drawable.background_splash_screen_title_6 -> {
                binding.splashScreenCircularProgressIndicator.setIndicatorColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.splash_screen_title_6_progress_bar
                    )
                )
                binding.splashScreenCircularProgressIndicator.trackColor =
                    ContextCompat.getColor(requireContext(), R.color.splash_screen_title_6)
            }
        }

        binding.splashScreenCircularProgressIndicator.visibility = View.VISIBLE

        binding.splashscreenImageView.setImageResource(splashscreenImage.image)
        binding.splashscreenTextView.apply {
            text = splashscreenImage.title
            setBackgroundResource(splashscreenImage.titleBackground)
            setTextColor(ContextCompat.getColor(context, splashscreenImage.titleColor))
        }

        lifecycleScope.launch {
            delay(3_000)
            authViewModel.authorization()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.splashScreenCircularProgressIndicator.visibility = View.GONE
        offFullScreen()
    }

    private fun onFullScreen() {
        val window: Window = requireActivity().window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun offFullScreen() {
        val window: Window = requireActivity().window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }
}
