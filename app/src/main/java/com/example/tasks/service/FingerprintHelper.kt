package com.example.tasks.service

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager

class FingerprintHelper {

    companion object {
        fun isAuthenticationAvailable(context: Context): Boolean {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false

            val biometricManager = BiometricManager.from(context)

            return when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS -> true
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> false
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> false
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
                else -> false
            }
        }
    }
}