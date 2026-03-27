package com.example.firemonitoringapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvSuhu: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnHistory: Button
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var lineChart: LineChart
    private lateinit var databaseReference: DatabaseReference
    
    private val entries = ArrayList<Entry>()
    private var count = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvSuhu = findViewById(R.id.tvSuhu)
        tvStatus = findViewById(R.id.tvStatus)
        btnHistory = findViewById(R.id.btnHistory)
        mainLayout = findViewById(R.id.mainLayout)
        lineChart = findViewById(R.id.lineChart)

        setupChart()
        requestNotificationPermission()

        val serviceIntent = Intent(this, FireMonitoringService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }

        btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("monitoring")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val suhuStr = snapshot.child("suhu").value.toString()
                    val status = snapshot.child("status").value.toString()
                    val suhu = suhuStr.toFloatOrNull() ?: 0f

                    tvSuhu.text = "$suhuStr °C"
                    tvStatus.text = "Status: $status"

                    updateUI(status)
                    updateChart(suhu)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setupChart() {
        lineChart.description.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.axisRight.isEnabled = false
    }

    private fun updateChart(suhu: Float) {
        entries.add(Entry(count++, suhu))
        if (entries.size > 20) entries.removeAt(0)

        val dataSet = LineDataSet(entries, "Suhu Realtime")
        dataSet.color = Color.BLUE
        dataSet.setCircleColor(Color.BLUE)
        dataSet.lineWidth = 2f
        dataSet.valueTextSize = 10f
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.CYAN

        lineChart.data = LineData(dataSet)
        lineChart.invalidate()
    }

    private fun updateUI(status: String) {
        if (status == "KEBAKARAN") {
            mainLayout.setBackgroundColor(Color.parseColor("#FFCDD2")) // Merah Muda
            tvStatus.setTextColor(Color.RED)
        } else {
            mainLayout.setBackgroundColor(Color.parseColor("#F5F5F5")) // Default
            tvStatus.setTextColor(Color.BLACK)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }
}