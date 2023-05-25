package com.bangkit.cloudraya.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.databinding.FirstOnboardBinding

class FragmentOnboarding : Fragment() {
    private lateinit var  binding : FirstOnboardBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View  {
        binding = FirstOnboardBinding.inflate(inflater, container ,false)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transition()
    }

    private fun transition(){
        binding.btnStart.setOnClickListener {
            binding.motionLayout.setTransition(R.id.toOnboard1)
            binding.motionLayout.transitionToEnd()
        }
        binding.btnContinue2.setOnClickListener {
            binding.motionLayout.setTransition(R.id.toOnboard2)
            binding.motionLayout.transitionToEnd()
        }
        binding.btnContinue3.setOnClickListener {
            binding.motionLayout.setTransition(R.id.toOnboard3)
            binding.motionLayout.transitionToEnd()
        }
        binding.btnContinue4.setOnClickListener {
            val toListSite =
                FragmentOnboardingDirections.actionFragmentOnboardingToFragmentSiteList()
            findNavController().navigate(toListSite)
        }

    }

}