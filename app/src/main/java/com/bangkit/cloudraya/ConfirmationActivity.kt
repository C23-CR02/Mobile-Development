package com.bangkit.cloudraya

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.bangkit.cloudraya.databinding.ActivityConfirmationBinding
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.Executor

class ConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmationBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hasBiometric()
    }

    private fun userAuth(){
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback(){
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
                Snackbar.make(binding.root, "Action accepted!", Snackbar.LENGTH_LONG).show()
                binding.tvMessage.text = "Action accepted!"
                Log.d("Auth", "OnSuccess")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Snackbar.make(binding.root, "Authentication Failed!", Snackbar.LENGTH_LONG).show()
                Log.d("Auth", "OnFailed")
            }

        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Action Confirmation")
            .setDescription("Scan to accept the action!")
            .setNegativeButtonText("Deny")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun hasBiometric(){
        val biometricManager = BiometricManager.from(this)
        when(biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)){
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
                binding.tvMessage.text = "Your device doesn't have fingerprint saved,please check your security settings"
            }
            else -> {
                Log.d("Bio Check", "Not Support")
            }
        }
    }
}