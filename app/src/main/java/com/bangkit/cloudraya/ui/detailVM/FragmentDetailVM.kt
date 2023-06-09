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
import com.bangkit.cloudraya.model.remote.DataAnomalyResponse
import com.bangkit.cloudraya.model.remote.DataGraphResponse
import com.bangkit.cloudraya.model.remote.VMData
import com.bangkit.cloudraya.model.remote.VMListData
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LegendEntry
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
                    Snackbar.make(binding.root, "Something went wrong..", Snackbar.LENGTH_SHORT)
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
        viewModel.setBaseUrl(siteUrl)
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
        viewModel.setBaseUrl(siteUrl)
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
        viewModel.getDataGraph(vmData.localId.toString())
        viewModel.dataGraph.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Event.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    setGraph(result.data)
                }
                is Event.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong..", Snackbar.LENGTH_SHORT)
                        .show()
                }
                is Event.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
            }
        }
        viewModel.getDataAnomaly(vmData.localId.toString())
        viewModel.dataAnomaly.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Event.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    getAnomaly(result.data)
                }
                is Event.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong..", Snackbar.LENGTH_SHORT)
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

    private fun setGraph(dataList: DataGraphResponse) {
        costProjection(dataList)
        additionalResources(dataList)
    }

    private fun costProjection(dataList: DataGraphResponse) {
        val lineChart = binding.costProjection
        val entries = ArrayList<Entry>()

        for (i in dataList.data) {
            val timestamp = getTimeFromTimestamp(i.timestamp)
            val forecast = i.forecasts.toFloat()
            val cost = i.cost.toFloat()

            entries.add(Entry(timestamp, cost))
        }
        val dataSet = LineDataSet(entries, "Forecast")
        dataSet.color = Color.BLUE

        val lineData = LineData(dataSet)

        lineChart.description = Description().apply { text = "" }
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.legend.isEnabled = true
        lineChart.data = lineData
        val dataCount = dataList.data.size
        val visibleRange = if (dataCount < 10) dataCount else 10
        lineChart.setVisibleXRange(0f, visibleRange.toFloat())
        lineChart.moveViewToX(2f)
        lineChart.invalidate()
    }

    private fun additionalResources(dataList: DataGraphResponse) {

        val lineChart = binding.additionalResources

        val entries = ArrayList<Entry>()
        val highlightEntries = ArrayList<Entry>()

        for (i in dataList.data) {
            val timestamp = getTimeFromTimestamp(i.timestamp)
            val forecast = i.forecasts.toFloat()
            val cost = i.cost.toFloat()

            entries.add(Entry(timestamp, forecast))
            if (forecast > 0.8) {
                highlightEntries.add(Entry(timestamp, forecast))
            }
        }


        val dataSet = LineDataSet(entries, "Forecast")
        dataSet.color = Color.BLUE

        val highlightDataSet = LineDataSet(highlightEntries, "Highlight")
        highlightDataSet.setDrawIcons(false)
        highlightDataSet.setDrawValues(false)
        highlightDataSet.circleRadius = 6f
        highlightDataSet.setCircleColor(Color.RED)

        highlightDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        val lineData = LineData(dataSet, highlightDataSet)

        lineChart.description = Description().apply { text = "" }
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        val legend = lineChart.legend
        legend.isEnabled = true
        val highlightEntry = LegendEntry()
        highlightEntry.label = "Highlight"
        highlightEntry.formColor = Color.RED
        val dataForecast = LegendEntry()
        dataForecast.label = "Forecast"
        dataForecast.formColor = Color.BLUE

        legend.setCustom(listOf(dataForecast, highlightEntry))

        lineChart.data = lineData
        val dataCount = dataList.data.size
        val visibleRange = if (dataCount < 10) dataCount else 10
        lineChart.setVisibleXRange(0f, visibleRange.toFloat())
        lineChart.moveViewToX(2f)
        lineChart.invalidate()

    }

    private fun getAnomaly(dataList: DataAnomalyResponse) {
        val lineChart = binding.anomalyDetection
        val entries = ArrayList<Entry>()
        val anomalyEntries = ArrayList<Entry>()
        for (i in dataList.data) {
            val timestamp = getTimeFromTimestamp(i.createdAt)
            val cpuUsed = i.cpuUsed.toFloat()
            val isAnomaly = i.isAnomaly

            entries.add(Entry(timestamp, cpuUsed))
            if (isAnomaly) {
                anomalyEntries.add(Entry(timestamp, cpuUsed))
            }
        }
        val dataSet = LineDataSet(entries, "Value")
        dataSet.color = Color.BLUE

        val highlightDataSet = LineDataSet(anomalyEntries, "Anomaly")
        highlightDataSet.setDrawIcons(false)
        highlightDataSet.setDrawValues(false)
        highlightDataSet.circleRadius = 6f
        highlightDataSet.setCircleColor(Color.RED)

        highlightDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        val lineData = LineData(dataSet, highlightDataSet)

        lineChart.description = Description().apply { text = "" }
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        val legend = lineChart.legend
        legend.isEnabled = true
        val highlightEntry = LegendEntry()
        highlightEntry.label = "Anomaly"
        highlightEntry.formColor = Color.RED
        val dataForecast = LegendEntry()
        dataForecast.label = "Value"
        dataForecast.formColor = Color.BLUE

        legend.setCustom(listOf(dataForecast, highlightEntry))

        lineChart.data = lineData
        val dataCount = dataList.data.size
        val visibleRange = if (dataCount < 10) dataCount else 10
        lineChart.setVisibleXRange(0f, visibleRange.toFloat())
        lineChart.moveViewToX(2f)
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