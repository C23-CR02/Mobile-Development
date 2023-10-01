package com.bangkit.cloudraya.ui.menu.billing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.databinding.FragmentBillingBinding
import com.bangkit.cloudraya.databinding.FragmentResources2Binding
import com.bangkit.cloudraya.ui.menu.dashboard.SharedViewModel
import com.bangkit.cloudraya.ui.menu.resources.ResourcesFragmentDirections
import com.bangkit.cloudraya.ui.menu.resources.ResourcesViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class BillingFragment : Fragment() {
    private lateinit var binding: FragmentBillingBinding
    private val sharedViewModel: SharedViewModel by inject()
    private var lastSelectedFragmentId = sharedViewModel.lastSelectedFragmentId.value

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationBar()
    }

    private fun bottomNavigationBar() {
        val navController = findNavController()
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.menu.findItem(R.id.navigation_billing).isChecked = true
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
                            BillingFragmentDirections.actionBillingFragmentToDashboardFragment()
                        navController.navigate(toDashboard)
                    }

                    R.id.navigation_resources -> {
                        val toResources =
                            BillingFragmentDirections.actionBillingFragmentToResourcesFragment()
                        navController.navigate(toResources)
                    }

                    R.id.navigation_networking -> {
                        val toNetwork =
                            BillingFragmentDirections.actionBillingFragmentToNetworkingFragment()
                        navController.navigate(toNetwork)
                    }

                    R.id.navigation_billing -> {

                    }
                }
            }
            true
        }
    }
}