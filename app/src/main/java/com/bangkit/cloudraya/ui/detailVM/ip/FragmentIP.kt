package com.bangkit.cloudraya.ui.detailVM.ip

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bangkit.cloudraya.databinding.FragmentIpBinding
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.DataItem
import com.bangkit.cloudraya.model.remote.IpBasicResponse
import com.bangkit.cloudraya.model.remote.IpPrivateItem
import com.bangkit.cloudraya.model.remote.IpPrivateResponse
import com.bangkit.cloudraya.model.remote.IpPublicResponse
import com.bangkit.cloudraya.model.remote.IpVMOwn
import com.bangkit.cloudraya.model.remote.PrivateIpsItem
import com.bangkit.cloudraya.model.remote.PublicIpsItem
import com.bangkit.cloudraya.ui.adapter.IpPrivateAdapter
import com.bangkit.cloudraya.ui.adapter.IpPublicAdapter
import com.bangkit.cloudraya.ui.detailVM.FragmentDetailVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentIP : Fragment() {
    private lateinit var binding: FragmentIpBinding
    private val viewModel: IpViewModel by viewModel()
    private lateinit var privateAdapter: IpPrivateAdapter
    private lateinit var publicAdapter: IpPublicAdapter
    private lateinit var token: String
    private lateinit var loc: String
    private lateinit var vmId: String
    private lateinit var siteUrl: String
    private lateinit var pDialog: SweetAlertDialog
    private lateinit var privateIps: List<PrivateIpsItem>
    private lateinit var publicIps: List<DataItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        buttonAction()
    }

    private fun buttonAction() {
        binding.btnAddPrivate.setOnClickListener {
            acquireIpPrivate()
        }
        binding.btnAddPublic.setOnClickListener {
            Toast.makeText(requireContext(), "Testing Acquire Public", Toast.LENGTH_SHORT).show()
            acquireIpPublic()
        }
        binding.btnAttachPublic.setOnClickListener {
            fetchIpPublicGeneral()
            fetchIpPrivate()
        }
    }

    private fun observeData() {
        token = arguments?.getString(FragmentDetailVM.ARG_TOKEN).toString()
        vmId = arguments?.getInt(FragmentDetailVM.ARG_VM_ID).toString()
        siteUrl = arguments?.getString(FragmentDetailVM.ARG_SITEURL).toString()
        loc = arguments?.getString(FragmentDetailVM.ARG_LOC).toString()
        fetchIpPrivate()
        fetchIpPublic()
    }

//    private fun fetchIpPrivate() {
//        val ipPrivateObserver = Observer<Event<IpPrivateResponse>> { result ->
//            when (result) {
//                is Event.Success -> {
//                    handleSuccess(result.data.data, binding.rvIpPrivate)
//                    privateIps = result.data.data
//                    Log.d("Testing", "${result.data.data}")
//                }
//
//                is Event.Loading -> handleLoading()
//                else -> handleError()
//            }
//        }
//        viewModel.getIpPrivate(token, siteUrl, vmId.toInt())
//            .observe(viewLifecycleOwner, ipPrivateObserver)
//    }
    private fun fetchIpPrivate() {
        val ipPrivateObserver = Observer<Event<IpVMOwn>> { result ->
            when (result) {
                is Event.Success -> {
                    handleSuccess(result.data.data.privateIps, binding.rvIpPrivate)
                    privateIps = result.data.data.privateIps.filter {
                        it.isUsed == 0
                    }
                    Log.d("Testing", "Private ${result.data.data.privateIps}")
                }

                is Event.Loading -> handleLoading()
                else -> handleError()
            }
        }
        viewModel.getIpVMOwn(token, vmId.toInt()).observe(viewLifecycleOwner, ipPrivateObserver)
    }

    private fun fetchIpPublic() {
        val ipPublicObserver = Observer<Event<IpVMOwn>> { result ->
            when (result) {
                is Event.Success -> {
                    handleSuccess(result.data.data.publicIps, binding.rvIpPublic)
                    Log.d("Testing", "Public ${result.data.data}")
                }

                is Event.Loading -> handleLoading()
                else -> handleError()
            }
        }

        viewModel.getIpVMOwn(token, vmId.toInt()).observe(viewLifecycleOwner, ipPublicObserver)
    }

    private fun handleSuccess(data: List<Any>?, recyclerView: RecyclerView) {
        binding.pbLoading.visibility = View.GONE
        if (recyclerView == binding.rvIpPrivate) {
            privateAdapter = IpPrivateAdapter(data as List<PrivateIpsItem>)
            deleteIpPrivate()
            recyclerView.adapter = privateAdapter

        } else {
            publicAdapter = IpPublicAdapter(data as List<PublicIpsItem>)
            detachIpPublic()
            recyclerView.adapter = publicAdapter
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun handleLoading() {
        binding.pbLoading.visibility = View.VISIBLE
    }

    private fun handleError() {
        binding.pbLoading.visibility = View.GONE
        pDialog.dismiss()
        Toast.makeText(requireContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
    }

    private fun deleteIpPrivate() {
        privateAdapter.setOnDeleteClickListener { ip ->
            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You won't be able to recover this Ip Address!")
                .setConfirmText("Delete")
                .setConfirmClickListener { sDialog ->
                    val deleteObserver = Observer<Event<IpBasicResponse>> { result ->
                        when (result) {
                            is Event.Success -> {
                                pDialog.dismissWithAnimation()
                                fetchIpPrivate()
                                SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Great!")
                                    .setContentText("Successfully deleted Ip Private").show()
                            }

                            is Event.Loading -> {
                                pDialog =
                                    SweetAlertDialog(
                                        requireContext(),
                                        SweetAlertDialog.PROGRESS_TYPE
                                    )
                                pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
                                pDialog.titleText = "Loading ..."
                                pDialog.setCancelable(true)
                                pDialog.show()
                            }

                            else -> handleError()
                        }
                    }
                    viewModel.deleteIpPrivate(token, ip, loc, vmId)
                        .observe(viewLifecycleOwner, deleteObserver)
                    sDialog.dismissWithAnimation()
                }
                .setCancelButton(
                    "Cancel"
                ) { sDialog -> sDialog.dismissWithAnimation() }
                .show()
        }
    }

    private fun acquireIpPrivate() {
        SweetAlertDialog(requireContext(), SweetAlertDialog.NORMAL_TYPE)
            .setTitleText("Acquire new Private IP")
            .setContentText("You need to manually configure the IP on the guest VM NIC after acquiring a new private ip. CloudRaya will not automatically configure the IP address obtained on the VM.")
            .setConfirmText("Acquire")
            .setConfirmClickListener { sDialog ->
                val acquireIpPrivateObserver = Observer<Event<IpBasicResponse>> { result ->
                    when (result) {
                        is Event.Success -> {
                            pDialog.dismissWithAnimation()
                            fetchIpPrivate()
                            SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Great!")
                                .setContentText("Successfully added Ip Private").show()
                        }

                        is Event.Loading -> {
                            pDialog =
                                SweetAlertDialog(
                                    requireContext(),
                                    SweetAlertDialog.PROGRESS_TYPE
                                )
                            pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
                            pDialog.titleText = "Loading ..."
                            pDialog.setCancelable(true)
                            pDialog.show()
                        }

                        else -> handleError()
                    }
                }
                viewModel.acquireIpPrivate(token, vmId)
                    .observe(viewLifecycleOwner, acquireIpPrivateObserver)
                sDialog.dismissWithAnimation()
            }
            .setCancelButton(
                "Cancel"
            ) { sDialog -> sDialog.dismissWithAnimation() }
            .show()
    }

    private fun acquireIpPublic() {
        showCustomInputDialog("add")
    }

    private fun showCustomInputDialog(action: String) {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        val titleTextView = TextView(requireContext())

        if (action == "add"){
            titleTextView.text = "Acquire Public IP"
        }else{
            titleTextView.text = "Attach Public IP"
        }

        val myPublicIp: MutableList<String> = mutableListOf()
        val myPrivateIp: MutableList<String> = mutableListOf()

        publicIps.forEach {
            myPublicIp.add(it.publicIp)
        }

        privateIps.forEach {
            myPrivateIp.add(it.ipaddress)
        }

        val subTitle1 = TextView(requireContext())
        subTitle1.text = "Public IP "

        val spinner = Spinner(requireContext())
//        val options = arrayOf("Pilihan 1", "Pilihan 2", "Pilihan 3")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, myPublicIp)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val subTitle2 = TextView(requireContext())
        subTitle2.text = "Private IP "

        val spinner2 = Spinner(requireContext())
//        val options2 = arrayOf("Pilihan 1", "Pilihan 2", "Pilihan 3")
        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, myPrivateIp)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = adapter2


        // Membuat params untuk mengatur margin Spinner
        val spinnerParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        spinnerParams.setMargins(24, 24, 24, 24) // Sesuaikan margin sesuai kebutuhan

        titleTextView.layoutParams = spinnerParams
        subTitle1.layoutParams = spinnerParams
        spinner.layoutParams = spinnerParams
        titleTextView.layoutParams = spinnerParams
        subTitle2.layoutParams = spinnerParams
        spinner2.layoutParams = spinnerParams

        layout.addView(titleTextView)
        layout.addView(subTitle1)
        layout.addView(spinner)
        layout.addView(subTitle2)
        layout.addView(spinner2)
        builder.setView(layout)

        builder.setPositiveButton("OK") { dialog, _ ->
            val ipPublicPosition = spinner.selectedItemPosition
            val ipPublicId = publicIps[ipPublicPosition].id
            val ipPrivate = spinner2.selectedItem
            Log.d("Attach", "$ipPublicId, $ipPrivate")
            attachIpPublic(ipPublicId, ipPrivate.toString())
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun attachIpPublic(ipPublicId: Int, ipPrivate: String){
        viewModel.attachIpPublic(token, ipPublicId, ipPrivate, vmId.toInt())
            .observe(viewLifecycleOwner){ result ->
                when (result) {
                    is Event.Success -> {
                        pDialog.dismiss()
                        Log.d("Testing","response : ${result.data.data}")
                        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Successful!")
                            .setContentText(result.data.message)
                            .show()
                        fetchIpPublic()
                        fetchIpPrivate()
                    }
                    is Event.Loading -> {
                        pDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
                        pDialog.titleText = "Loading"
                        pDialog.setCancelable(false)
                        pDialog.show()
                    }
                    is Event.Error -> {
                        pDialog.dismiss()
                        Log.d("Testing","response : ${result.error}")
                        SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(result.error)
                            .show()
                    }
                }

        }
    }

    private fun detachIpPublic() {
        publicAdapter.setOnDetachClickListener { localId ->
            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Detach this Public IP?")
                .setContentText("This Public IP will be detached from your VM")
                .setConfirmText("Detach")
                .setConfirmClickListener { sDialog ->
                    val detachObserver = Observer<Event<IpBasicResponse>> { result ->
                        when (result) {
                            is Event.Success -> {
                                pDialog.dismissWithAnimation()
                                SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Successful!")
                                    .setContentText(result.data.message).show()
                                fetchIpPublic()
                                fetchIpPrivate()
                            }

                            is Event.Loading -> {
                                pDialog =
                                    SweetAlertDialog(
                                        requireContext(),
                                        SweetAlertDialog.PROGRESS_TYPE
                                    )
                                pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
                                pDialog.titleText = "Loading ..."
                                pDialog.setCancelable(true)
                                pDialog.show()
                            }

                            else -> {
                                Log.d("onDetach", result.getContentIfNotHandled()?.message ?: "a")
                                handleError()
                            }
                        }
                    }
                    viewModel.detachIpPublic(token, localId, vmId.toInt())
                        .observe(viewLifecycleOwner, detachObserver)
                    sDialog.dismissWithAnimation()
                }
                .setCancelButton(
                    "Cancel"
                ) { sDialog -> sDialog.dismissWithAnimation() }
                .show()
        }
    }

    private fun fetchIpPublicGeneral() {
        val ipPublicGeneralObserver = Observer<Event<IpPublicResponse>> { result ->
            when (result) {
                is Event.Success -> {
                    publicIps = result.data.data.filter {
                        it.objecttype.isEmpty()
                    }
                    Log.d("Testing", "Public ${result.data.data}")
                    showCustomInputDialog("attach")
                }

                is Event.Loading -> handleLoading()
                else -> handleError()
            }
        }
        viewModel.getIpPublic(token, siteUrl).observe(viewLifecycleOwner, ipPublicGeneralObserver)
    }
}

