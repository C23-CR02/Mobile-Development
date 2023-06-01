package com.bangkit.cloudraya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bangkit.cloudraya.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent

        val clickAction = intent.getStringExtra("click_action")
        if (clickAction == "ACTION_CONFIRMATION"){
            val title = intent.getStringExtra("title")
            val msgBody = intent.getStringExtra("body")
            Log.d("MyNotification", "Target: $clickAction, $title, $msgBody")
            startActivity(Intent(this, ConfirmationActivity::class.java).apply {
                putExtra("title", title)
                putExtra("body", msgBody)
            })
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
        if (navHostFragment is NavHostFragment) {
            navController = navHostFragment.navController
        }
    }
}