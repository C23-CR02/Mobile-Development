package com.bangkit.cloudraya.ui.siteList

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cloudraya.MainActivity
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.databinding.FragmentSiteListBinding
import com.bangkit.cloudraya.model.local.DataHolder
import com.bangkit.cloudraya.ui.adapter.SiteListAdapter
import com.bangkit.cloudraya.websocket.WebSocketService
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSiteList : Fragment() {
    private lateinit var binding: FragmentSiteListBinding
    private val viewModel: SiteListViewModel by viewModel()
    private lateinit var adapter: SiteListAdapter
    private lateinit var webSocketService: WebSocketService
    private var isServiceBound = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSiteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegisterSite.setOnClickListener {
            toSiteAdd()
        }
        toDetailVM()
        backPressed()
//        val serviceIntent = Intent(requireContext(), WebSocketService::class.java)
//        requireContext().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
//        val siteName = viewModel.getProject()
//        val data = viewModel.getList(siteName.toString())
//        val appKey = data[0]
//        broadcast(appKey.toString())

    }


    private fun showConfirmationDialog(site: Sites) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi Penghapusan")
        builder.setMessage("Apakah Anda yakin ingin menghapus data?")
        builder.setPositiveButton("Ya") { dialog, _ ->
            lifecycleScope.launch {
                viewModel.removeSite(site)
                adapter.updateData(viewModel.getSites())
                dialog.dismiss()
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun toDetailVM() {
        lifecycleScope.launch {
            val site = viewModel.getSites()
            adapter = SiteListAdapter(site)
            val layoutManager = LinearLayoutManager(requireContext())
            adapter.setOnItemClickCallback(object : SiteListAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Sites) {
                    showSelectedSite(data)
                }
            })
            adapter.setOnItemLongClickCallback(object : SiteListAdapter.OnItemClickLongCallback {
                override fun onItemLongClicked(data: Sites) {
                    showConfirmationDialog(data)
                }
            })
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = layoutManager
            if (site.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.ivEmpty.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.ivEmpty.visibility = View.GONE
            }
        }
    }


    private fun backPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                showExitConfirmationDialog()
                findNavController().navigateUp()
            }
        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.exit_confirmation))
            .setMessage(getString(R.string.exit_confirmation_message))
            .setPositiveButton(getString(R.string.exit)) { _, _ ->
                requireActivity().finish()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun showSelectedSite(data: Sites) {
        viewModel.setBaseUrl(data.site_url)
        setProject(data.site_name)
        toDashboard()
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as WebSocketService.LocalBinder
            Log.d("Testing", "sedang mencoba service")
            webSocketService = binder.getService()
            Log.d("Testing", "Berhasil masuk service")
            isServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBound = false
            Log.d("Testing", "Gagal masuk service")
        }
    }

    private fun broadcast(channelKey: String) {
        val intent = Intent(context, MainActivity::class.java)
        intent.action = "ACTION_ADD_CHANNEL"
        requireContext().sendBroadcast(intent)
        val dataHolder: DataHolder by inject()
        dataHolder.channelKey = channelKey
        webSocketService.joinChannel()
    }


    private fun toDashboard() {
        val toDashboard = FragmentSiteListDirections.actionFragmentSiteListToDashboardFragment()
        findNavController().navigate(toDashboard)
    }

    private fun toSiteAdd() {
        val toSiteEdit = FragmentSiteListDirections.actionFragmentSiteListToFragmentSiteAdd()
        findNavController().navigate(toSiteEdit)
    }

    private fun setProject(key: String) {
        viewModel.setProject(key)
    }
}