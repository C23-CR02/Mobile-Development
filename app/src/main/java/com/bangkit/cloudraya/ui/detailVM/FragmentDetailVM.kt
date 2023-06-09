package com.bangkit.cloudraya.ui.detailVM

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.databinding.FragmentDetailVmBinding
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.DataGraphResponseItem
import com.bangkit.cloudraya.model.remote.VMData
import com.bangkit.cloudraya.model.remote.VMListData
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class FragmentDetailVM : Fragment() {
    private lateinit var binding: FragmentDetailVmBinding
    private val viewModel: DetailVMViewModel by viewModel()
    private lateinit var vmData: VMListData
    private lateinit var site: String
    private lateinit var siteUrl: String

    private var token = ""
    private lateinit var pDialog: SweetAlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailVmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmData = FragmentDetailVMArgs.fromBundle(arguments as Bundle).dataVM
        site = arguments?.getString("site") ?: ""
        siteUrl = arguments?.getString("siteUrl") ?: ""
        Log.d("Testing", "Site url : $siteUrl")

        val data = viewModel.getListEncrypted(site)
        token = data[2].toString()

        observeData(vmData)
        binding.btnBack.setOnClickListener {
            backPressed()
        }
        binding.btnStart.setOnClickListener {
            startVM()
        }
        binding.btnStop.setOnClickListener {
            stopVM()
        }
        binding.btnRestart.setOnClickListener {
            restartVM()
        }
        backPressed()
    }

    private fun observeData(vmData: VMListData) {
        viewModel.setBaseUrl(siteUrl)
        viewModel.getVMDetail(token, vmData.localId!!)
        viewModel.vmDetail.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Event.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    updateUI(result.data.data!!)
                    getGraph()

                }
                is Event.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    Snackbar.make(binding.root, result.error ?: "Error", Snackbar.LENGTH_SHORT)
                        .show()
                }
                is Event.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
            }
        }

    }


    private fun startVM() {
        viewModel.setBaseUrl(siteUrl)
        viewModel.vmAction(token, vmData.localId!!, "start")
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Event.Success -> {
                        pDialog.dismiss()
                        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Successful!")
                            .setContentText(result.data.message)
                            .show()
                        runBlocking {
                            delay(Toast.LENGTH_LONG.toLong())
                        }
                        observeData(vmData)
                    }
                    is Event.Error -> {
                        pDialog.dismiss()
                        SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(result.error)
                            .show()
                    }
                    is Event.Loading -> {
                        pDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
                        pDialog.titleText = "Loading"
                        pDialog.setCancelable(false)
                        pDialog.show()
                    }
                }
            }
    }

    private fun stopVM() {
        viewModel.setBaseUrl(site)
        viewModel.vmAction(token, vmData.localId!!, "stop")
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Event.Success -> {
                        pDialog.dismiss()
                        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Successful!")
                            .setContentText(result.data.message)
                            .show()
                        runBlocking {
                            delay(Toast.LENGTH_LONG.toLong())
                        }
                        observeData(vmData)
                    }
                    is Event.Error -> {
                        pDialog.dismiss()
                        SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(result.error)
                            .show()
                    }
                    is Event.Loading -> {
                        pDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
                        pDialog.titleText = "Loading"
                        pDialog.setCancelable(false)
                        pDialog.show()
                    }
                }
            }
    }

    private fun restartVM() {
        viewModel.setBaseUrl(site)
        viewModel.vmAction(token, vmData.localId!!, "reboot")
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Event.Success -> {
                        pDialog.dismiss()
                        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Successful!")
                            .setContentText(result.data.message)
                            .show()
                        runBlocking {
                            delay(Toast.LENGTH_LONG.toLong())
                        }
                        observeData(vmData)
                    }
                    is Event.Error -> {
                        pDialog.dismiss()
                        Snackbar.make(
                            binding.root,
                            result.error ?: "Error",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                    is Event.Loading -> {
                        pDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
                        pDialog.titleText = "Loading"
                        pDialog.setCancelable(false)
                        pDialog.show()
                    }
                }
            }
    }

    private fun updateUI(data: VMData) {
        binding.apply {
            tvName.text = data.hostname
            tvLocation.text = data.location
            tvImage.text = data.os
            tvIPAddress.text = data.publicIp
            tvLaunchDate.text = data.launchDate
            tvStatus.text = data.state
        }
        if (data.state!!.contains("stop", ignoreCase = true)) {
            binding.apply {
                btnStop.isEnabled = false
                btnStart.isEnabled = true
                btnRestart.isEnabled = false
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_inactive, 0, 0, 0)
            }
        } else {
            binding.apply {
                btnStop.isEnabled = true
                btnStart.isEnabled = false
                btnRestart.isEnabled = true
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_active, 0, 0, 0)
            }
        }
    }

    private fun getGraph() {
        viewModel.setBaseUrl("https://backend-dot-mobile-notification-90a3a.et.r.appspot.com")
        viewModel.getDataGraph()
        viewModel.dataGraph.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Event.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    setGraph(result.data)
                }
                is Event.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    Snackbar.make(binding.root, result.error ?: "Error", Snackbar.LENGTH_SHORT)
                        .show()
                }
                is Event.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun backPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val back = FragmentDetailVMDirections.actionFragmentDetailVMToFragmentResources(
                    site,
                    siteUrl
                )
                findNavController().navigate(back)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private fun setGraph(data: Any) {
        val lineChart = binding.chartCostProjection

        val entries = ArrayList<Entry>()
        val trimmedData = data.toString().substring(1, data.toString().length - 1)
        Log.d("Testing", "Trimmed data $trimmedData")
        val pairs = trimmedData.split("\\}, \\{".toRegex())
        val dataList = mutableListOf<DataGraphResponseItem>()
        for (pair in pairs) {
            val trimmedPair = pair.trim('{', '}')
            val keyValuePairs = trimmedPair.split(", ")
            val dataMap = mutableMapOf<String, String>()
            for (keyValuePair in keyValuePairs) {
                val (key, value) = keyValuePair.split("=")

                dataMap[key] = value
            }
            val dataItem = DataGraphResponseItem(
                timestamp = dataMap["Timestamp"] ?: "",
                forecasts = dataMap["Forecasts"] ?: "",
                cost = dataMap["Cost"] ?: "",
                core = dataMap["Core"] ?: ""
            )
            dataList.add(dataItem)
        }
        Log.d("Testing", "data $dataList")

        for (i in dataList) {
            val timestamp = getTimeFromTimestamp(i.timestamp)
            val forecast = i.forecasts.toFloat()
            val cost = i.cost.toFloat()

            entries.add(Entry(timestamp, forecast))
        }

        val dataSet = LineDataSet(entries, "Forecast")
        dataSet.color = Color.BLUE
        val lineData = LineData(dataSet)

        lineChart.description = Description().apply { text = "" }
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.legend.isEnabled = false
        lineChart.data = lineData
        lineChart.invalidate()
    }

    private fun getTimeFromTimestamp(timestamp: String): Float {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateFormat.parse(timestamp)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return hour.toFloat()
    }
}