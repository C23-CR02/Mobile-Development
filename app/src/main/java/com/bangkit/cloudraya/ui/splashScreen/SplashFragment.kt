package com.bangkit.cloudraya.ui.splashScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bangkit.cloudraya.databinding.FragmentSplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashFragment : Fragment() {
    private lateinit var binding : FragmentSplashBinding
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

        CoroutineScope(Dispatchers.Main).launch {
            delay(DURATION)
            if(onBoarding){
                toList()
            } else {
                toOnBoarding()
            }
        }
    }

    private fun toList(){
        val toList = SplashFragmentDirections.actionSplashFragmentToFragmentSiteList()
        findNavController().navigate(toList)
    }
    private fun toOnBoarding(){
        val toOnBoarding = SplashFragmentDirections.actionSplashFragmentToFragmentOnboarding()
        findNavController().navigate(toOnBoarding)
    }

    companion object{
        const val DURATION = 1000L
    }
}