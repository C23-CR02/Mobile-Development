package com.bangkit.cloudraya.ui.menu.networking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.databinding.FragmentNetworkingBinding
import com.bangkit.cloudraya.ui.menu.dashboard.SharedViewModel
import org.koin.android.ext.android.inject


class NetworkingFragment : Fragment() {
    private lateinit var binding: FragmentNetworkingBinding
    private val sharedViewModel: SharedViewModel by inject()
    private var lastSelectedFragmentId = sharedViewModel.lastSelectedFragmentId.value

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNetworkingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationBar()
    }

    private fun bottomNavigationBar() {
        val navController = findNavController()
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.menu.findItem(R.id.navigation_networking).isChecked = true
        when (lastSelectedFragmentId) {
            R.id.navigation_dashboard -> {
                bottomNavigationView.menu.findItem(R.id.navigation_dashboard).isChecked = true
            }

            R.id.navigation_resources -> {
                bottomNavigationView.menu.findItem(R.id.navigation_resources).isChecked = true
            }

            R.id.navigation_networking -> {
                bottomNavigationView.menu.findItem(R.id.navigation_networking).isChecked = true
            }

            R.id.navigation_billing -> {
                bottomNavigationView.menu.findItem(R.id.navigation_billing).isChecked = true
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            if (item.itemId != lastSelectedFragmentId) {
                when (item.itemId) {
                    R.id.navigation_dashboard -> {
                        val toDashboard =
                            NetworkingFragmentDirections.actionNetworkingFragmentToDashboardFragment()
                        navController.navigate(toDashboard)
                    }

                    R.id.navigation_resources -> {
                        val toResources =
                            NetworkingFragmentDirections.actionNetworkingFragmentToResourcesFragment()
                        navController.navigate(toResources)
                    }

                    R.id.navigation_networking -> {

                    }

                    R.id.navigation_billing -> {
                        val toBilling =
                            NetworkingFragmentDirections.actionNetworkingFragmentToBillingFragment()
                        navController.navigate(toBilling)
                    }
                }
            }
            true
        }
    }
}