package com.bangkit.cloudraya

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bangkit.cloudraya.databinding.ActivityMainBinding
import com.bangkit.cloudraya.websocket.WebSocketService
import com.bangkit.cloudraya.model.local.DataHolder
import com.bangkit.cloudraya.ui.confirmation.ConfirmationFragment
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), ConfirmationFragment.ConfirmationFragmentListener {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private var webSocketService: WebSocketService? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0
            )
        }
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        handleNotificationAction(intent)
        val serviceIntent = Intent(this, WebSocketService::class.java).also {
            it.action = WebSocketService.Actions.START.toString()
            startService(it)
        }
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as WebSocketService.LocalBinder
            webSocketService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            webSocketService = null
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Tangani action dari notifikasi
        handleNotificationAction(intent)
    }

    private fun handleNotificationAction(intent: Intent?) {
        if (intent?.action == "ACTION_CONFIRMATION_FRAGMENT") {
            val dataHolder: DataHolder by inject()
            dataHolder.isPressed = true
            navigateToConfirmationFragment()
        } else if (intent?.action == "ACTION_ADD_CHANNEL") {
            val channelKey = intent.getStringExtra("channelKey")
            if (channelKey != null) {
                webSocketService?.joinChannel()
                Log.d("Testing", "joinChannel dipanggil dengan channelKey: $channelKey")
            }
            webSocketService?.joinChannel()
        } else {
            navController = navHostFragment.navController
        }
    }


    override fun navigateToConfirmationFragment() {
        navController = navHostFragment.navController
        navController.navigate(R.id.confirmationFragment)
    }
}

