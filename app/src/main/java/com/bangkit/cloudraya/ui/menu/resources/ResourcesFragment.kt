package com.bangkit.cloudraya.ui.menu.resources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.databinding.FragmentResources2Binding
import com.bangkit.cloudraya.ui.menu.dashboard.SharedViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResourcesFragment : Fragment() {
    private lateinit var binding: FragmentResources2Binding
    private val viewModel: ResourcesViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by inject()
    private var lastSelectedFragmentId = sharedViewModel.lastSelectedFragmentId.value
    private var siteName: String? = ""
    private var siteUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResources2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationBar()
        navigation()
        siteName = viewModel.getProject()
        val data = viewModel.getList(siteName.toString())
        siteUrl = data[3].toString()
    }

    private fun navigation() {
        binding.cardVm.setOnClickListener {
            toVM()
        }
    }

    private fun toVM() {
        val toVM = ResourcesFragmentDirections.actionResourcesFragmentToFragmentResources(
            siteName.toString(),
            siteUrl
        )
        findNavController().navigate(toVM)
    }

    private fun bottomNavigationBar() {
        val navController = findNavController()
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.menu.findItem(R.id.navigation_resources).isChecked = true
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
                            ResourcesFragmentDirections.actionResourcesFragmentToDashboardFragment()
                        navController.navigate(toDashboard)
                    }

                    R.id.navigation_resources -> {
                    }

                    R.id.navigation_networking -> {
                        val toNetwork =
                            ResourcesFragmentDirections.actionResourcesFragmentToNetworkingFragment()
                        navController.navigate(toNetwork)
                    }

                    R.id.navigation_billing -> {
                        val toBilling =
                            ResourcesFragmentDirections.actionResourcesFragmentToBillingFragment()
                        navController.navigate(toBilling)
                    }
                }
            }
            true
        }
    }
}