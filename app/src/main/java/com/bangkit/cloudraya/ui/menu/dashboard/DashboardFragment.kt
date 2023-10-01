package com.bangkit.cloudraya.ui.menu.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.databinding.FragmentDashboardBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: DashboardViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by inject()
    private var lastSelectedFragmentId = sharedViewModel.lastSelectedFragmentId.value

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.projectLayout.setOnClickListener {
            navigateProject()
        }
        bottomNavigationBar()
        val project = getProject()
        binding.projectInput.text = project
    }


    private fun navigateProject() {
        val toProject = DashboardFragmentDirections.actionDashboardFragmentToFragmentSiteList()
        findNavController().navigate(toProject)
    }

    private fun getProject(): String? {
        return viewModel.getProject()
    }

    private fun bottomNavigationBar() {
        val bottomNavigationView = binding.bottomNavigationView
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
                    }

                    R.id.navigation_resources -> {
                        val toResources =
                            DashboardFragmentDirections.actionDashboardFragmentToResourcesFragment()
                        findNavController().navigate(toResources)
                    }

                    R.id.navigation_networking -> {
                        val toNetwork =
                            DashboardFragmentDirections.actionDashboardFragmentToNetworkingFragment()
                        findNavController().navigate(toNetwork)
                    }

                    R.id.navigation_billing -> {
                        val toBilling =
                            DashboardFragmentDirections.actionDashboardFragmentToBillingFragment()
                        findNavController().navigate(toBilling)
                    }
                }
            }
            true
        }
    }
}