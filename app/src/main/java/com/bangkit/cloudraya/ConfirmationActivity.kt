package com.bangkit.cloudraya

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bangkit.cloudraya.databinding.ActivityConfirmationBinding
import com.bangkit.cloudraya.model.local.Event
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.Executor

class ConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmationBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val viewModel: ConfirmationActivityViewModel by viewModel()
    private var msgBody: String = "Action Confirmation"
    private var token: String = "Bearer "
    private var request: String = ""
    private var siteUrl: String = ""
    private var vmId: String = ""
    private lateinit var pDialog: SweetAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hasBiometric()
    }

    private fun vmAction() {
        val intent = intent
        token += intent.getStringExtra("token").toString()
        request += intent.getStringExtra("request").toString()
        siteUrl += intent.getStringExtra("site_url").toString()
        vmId += intent.getStringExtra("vmId")
        val title = intent.getStringExtra("title")
        val msgBody = intent.getStringExtra("body")
        Log.d("Testing", "Target:  $title, $msgBody, $token, $request, $siteUrl, $vmId")
        viewModel.setBaseUrl(siteUrl)
        viewModel.vmAction(token, vmId.toInt(), request)
            .observe(this) { result ->
                when (result) {
                    is Event.Success -> {
                        pDialog.dismiss()
                        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Successful!")
                            .setContentText(result.data.message)
                            .show()
                        runBlocking {
                            delay(Toast.LENGTH_LONG.toLong())
                        }
                        Log.d("Testing", "Success")
                    }
                    is Event.Error -> {
                        pDialog.dismiss()
                        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(result.error)
                            .show()
                    }
                    is Event.Loading -> {
                        pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                        pDialog.titleText = "Loading"
                        pDialog.setCancelable(false)
                        pDialog.show()
                    }
                }
            }
    }

    private fun userAuth() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    val userCancelled = errorCode == BiometricPrompt.ERROR_USER_CANCELED
                    if (userCancelled) {
                        Snackbar.make(binding.root, "Action denied!", Snackbar.LENGTH_LONG).show()
                    } else {
                        Snackbar.make(binding.root, "Action denied!", Snackbar.LENGTH_LONG).show()
                    }
                    Log.d("Auth", "OnError code = $errorCode")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    sendResponseToFirebase()
                    vmAction()
                    Snackbar.make(binding.root, "Action accepted!", Snackbar.LENGTH_LONG).show()
                    binding.tvMessage.text = "Action accepted!"
                    Log.d("Auth", "OnSuccess")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Snackbar.make(binding.root, "Authentication Failed!", Snackbar.LENGTH_LONG)
                        .show()
                    Log.d("Auth", "OnFailed")
                }

            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(msgBody)
            .setDescription("Scan to accept the action!")
            .setNegativeButtonText("Deny")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun hasBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                userAuth()
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                binding.tvMessage.text = "No biometric features available on this device."
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                binding.tvMessage.text = "Biometric features are currently unavailable."
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                binding.tvMessage.text =
                    "Your device doesn't have fingerprint saved,please check your security settings"
            }
            else -> {
                Log.d("Bio Check", "Not Support")
            }
        }
    }

    private fun sendResponseToFirebase() {
        // Inisialisasi FirebaseApp jika belum dilakukan sebelumnya
        FirebaseApp.initializeApp(this)

        // Ambil instance FirebaseMessaging untuk mengirim pesan ke server Firebase
        val firebaseMessaging = FirebaseMessaging.getInstance()

        // Buat data yang akan dikirim ke server Firebase
        val data = hashMapOf(
            "response" to "fingerprint_success"
        )

        // Kirim pesan dengan data ke server Firebase
        firebaseMessaging.send(
            RemoteMessage.Builder("1044426049361@gcm.googleapis.com")
                .setMessageId(Random().nextInt(1000000).toString())
                .setData(data)
                .build()
        )
    }

    private fun toMenu() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}