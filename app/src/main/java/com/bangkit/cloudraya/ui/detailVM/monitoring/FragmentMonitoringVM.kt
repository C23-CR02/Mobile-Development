package com.bangkit.cloudraya.ui.detailVM.monitoring

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.databinding.FragmentMonitoringVmBinding
import com.bangkit.cloudraya.ui.detailVM.FragmentDetailVM
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.DataAnomalyResponse
import com.bangkit.cloudraya.model.remote.DataGraphResponse
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.text.SimpleDateFormat
import java.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentMonitoringVM : Fragment() {
    private lateinit var binding: FragmentMonitoringVmBinding
    private val viewModel: MonitoringVMViewModel by viewModel()
    private var vm_id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMonitoringVmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm_id = arguments?.getInt(FragmentDetailVM.ARG_VM_ID).toString()
        getGraph()
    }


    private fun getGraph() {
        viewModel.getDataGraph(vm_id)
        viewModel.dataGraph.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Event.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    setGraph(result.data)
                }
                is Event.Error -> {
                    binding.pbLoading.visibility = View.GONE
                }
                is Event.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
            }
        }
        viewModel.getDataAnomaly(vm_id)
        viewModel.dataAnomaly.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Event.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    getAnomaly(result.data)
                }
                is Event.Error -> {
                    binding.pbLoading.visibility = View.GONE
                }
                is Event.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun setGraph(dataList: DataGraphResponse) {
        costProjection(dataList)
        additionalResources(dataList)
    }

    private fun costProjection(dataList: DataGraphResponse) {
        val lineChart = binding.costProjection
        val entries = ArrayList<Entry>()
        var totalCost = 0f

        for (i in dataList.data) {
            val timestamp = getTimeFromTimestamp(i.timestamp)
            val forecast = i.forecasts.toFloat()
            val cost = i.cost.toFloat()
            totalCost += cost
            entries.add(Entry(timestamp, cost))
        }
        binding.tvEstimatedCost.text = String.format(getString(R.string.estimated_cost), totalCost)
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