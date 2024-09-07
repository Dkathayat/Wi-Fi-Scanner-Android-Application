package com.kathayat.railscarmaapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Paint
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.kathayat.railscarmaapplication.customviews.GridOverlayView
import com.kathayat.railscarmaapplication.customviews.WifiMarkerView
import com.kathayat.railscarmaapplication.databinding.ActivityMainBinding
import com.kathayat.railscarmaapplication.room.WifiData
import com.kathayat.railscarmaapplication.utils.DialogBuilder
import com.kathayat.railscarmaapplication.viewmodel.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val roomViewModel: RoomViewModel by viewModels()

    private lateinit var wifiManager: WifiManager
    private lateinit var wifiMarkerView: WifiMarkerView
    private lateinit var dialogBuilder: DialogBuilder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialogBuilder = DialogBuilder(this)

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiMarkerView = binding.wifiMarkerView

        lifecycleScope.launch {
            roomViewModel.items.collect() {
                Log.d("RoomData", it.toString())
            }
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            scanWiFiNetworks()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scanWiFiNetworks()
        } else {
            dialogBuilder.showDialog("Permission Missing!!" ,"Location permission is required to scan Wi-Fi networks.Please goto app setting and enable the location permission")
        }
    }
    private fun scanWiFiNetworks() {
        showDialog("Scanning WIFI","Please wait while we scan for all the available wifi...")
        registerReceiver(wifiReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        wifiManager.startScan()

    }
    private val wifiReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            val results = wifiManager.scanResults
            unregisterReceiver(this)
            plotWiFiSignals(results)
        }
    }

    // Load data from wifi first not available then room if room not available fake wifi
    private fun plotWiFiSignals(results: List<ScanResult>) {
        if (results.isEmpty()) {
           roomViewModel.loadRoomData().observe(this){wifiDataList ->
               if (wifiDataList.isNullOrEmpty()){
                   val fakeWifiList = listOf(
                       Pair("FakeWiFi1", -40),
                       Pair("FakeWiFi2", -60),
                       Pair("FakeWiFi3", -50),
                       Pair("FakeWiFi4", -80),
                       Pair("FakeWiFi5", -20),
                       Pair("FakeWiFi6", -30),
                       Pair("FakeWiFi7", -10)
                   )
                   for (wifi in fakeWifiList) {
                       wifiMarkerView.addMarker(wifi.first, wifi.second)
                   }
               } else {
                   wifiDataList.forEach { wifiData ->
                       wifiMarkerView.addMarker(wifiData.name, wifiData.strenght)
                   }
               }
           }

        } else {
            for (result in results) {
                val ssid = result.SSID
                val signalStrength = result.level
                wifiMarkerView.addMarker(ssid, signalStrength)
                // Store wifi data in room 'with each unique name
                roomViewModel.insert(WifiData.createWifiData(ssid,signalStrength))
            }
        }
        binding.progessDialog.visibility = View.GONE
    }

    private fun showDialog(title:String, message:String){
        binding.dialogTitle.text = title
        binding.dialogMessage.text = message
        binding.progessDialog.visibility = View.VISIBLE
    }


    override fun onResume() {
        super.onResume()
        wifiMarkerView.invalidate()
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}