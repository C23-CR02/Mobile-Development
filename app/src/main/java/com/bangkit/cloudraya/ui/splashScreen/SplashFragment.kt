package com.bangkit.cloudraya.ui.splashScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bangkit.cloudraya.databinding.FragmentSplashBinding
import com.bangkit.cloudraya.model.local.DataHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private val viewModel: SplashScreenViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val onBoarding = viewModel.getOnboarding()
        val dataHolder: DataHolder by inject()
        CoroutineScope(Dispatchers.Main).launch {
            delay(DURATION)
            if (onBoarding && (dataHolder.action != "ACTION_CONFIRMATION_FRAGMENT") && !dataHolder.isPressed) {
                if (!viewModel.getProject().isNullOrEmpty()) {
                    toDashboard()
                } else {
                    toList()
                }
            } else if (!onBoarding) {
                toOnBoarding()
            }
        }
    }

    private fun toDashboard() {
        val toDashboard = SplashFragmentDirections.actionSplashFragmentToDashboardFragment()
        findNavController().navigate(toDashboard)
    }

    private fun toList() {
        val toList = SplashFragmentDirections.actionSplashFragmentToFragmentSiteList()
        findNavController().navigate(toList)
    }

    private fun toOnBoarding() {
        val toOnBoarding = SplashFragmentDirections.actionSplashFragmentToFragmentOnboarding()
        findNavController().navigate(toOnBoarding)
    }

    companion object {
        const val DURATION = 1000L
    }
}