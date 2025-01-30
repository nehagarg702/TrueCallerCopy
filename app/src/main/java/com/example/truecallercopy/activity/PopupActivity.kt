package com.example.truecallercopy.activity

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class PopupActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.values.all { it }) {
                checkOverlayPermission()
            } else {
                showPermissionDeniedMessage("Some permissions were denied. The app may not function properly.")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        checkPermissions()
    }

    private fun checkPermissions() {
        val requiredPermissions = mutableListOf<String>()

        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.READ_PHONE_STATE)
        }
        if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.READ_CALL_LOG)
        }
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.READ_CONTACTS)
        }

        if (requiredPermissions.isNotEmpty()) {
            requestPermissionsLauncher.launch(requiredPermissions.toTypedArray())
        } else {
            checkOverlayPermission()
        }
    }

    private fun checkOverlayPermission() {
        if (!hasOverlayPermission()) {
            showOverlayPermissionDialog()
        } else {
            checkAutoStartPermission()
        }
    }

    private fun showOverlayPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Enable Overlay Permission")
            .setMessage("Overlay permission is required to display caller ID on incoming calls.")
            .setPositiveButton("Allow") { _, _ ->
                requestOverlayPermission()
            }
            .setNegativeButton("Deny") { _, _ ->
                Toast.makeText(this, "The app may not work properly without overlay permission.", Toast.LENGTH_LONG).show()
                checkAutoStartPermission()
            }
            .setCancelable(false)
            .show()
    }

    private fun requestOverlayPermission() {
        val overlayIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
            data = android.net.Uri.parse("package:$packageName")
        }
        startActivityForResult(overlayIntent, 1001) // Start overlay permission request
    }

    private fun hasOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001) { // Overlay permission result
            if (hasOverlayPermission()) {
                sharedPreferences.edit().putBoolean("overlay_permission_granted", true).apply()
                checkAutoStartPermission() // Move to Auto-Start permission check
            } else {
                Toast.makeText(this, "Overlay permission denied. Some features may not work.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkAutoStartPermission() {
        if (!isAutoStartPermissionGranted()) {
            showAutoStartDialog()
        }
    }

    private fun showAutoStartDialog() {
        AlertDialog.Builder(this)
            .setTitle("Enable Auto-Start")
            .setMessage("Auto-Start permission is required to ensure the app works properly in the background.")
            .setPositiveButton("Allow") { _, _ ->
                requestAutoStartPermission()
            }
            .setNegativeButton("Deny") { _, _ ->
                Toast.makeText(this, "The app may not work properly without Auto-Start permission.", Toast.LENGTH_LONG).show()
            }
            .setCancelable(false)
            .show()
    }

    private fun isAutoStartPermissionGranted(): Boolean {
        return sharedPreferences.getBoolean("auto_start_granted", false)
    }

    private fun requestAutoStartPermission() {
        try {
            val intent = when (android.os.Build.MANUFACTURER.lowercase()) {
                "xiaomi" -> Intent().apply {
                    setClassName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
                }
                "oppo" -> Intent().apply {
                    setClassName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")
                }
                "vivo" -> Intent().apply {
                    setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")
                }
                "letv" -> Intent().apply {
                    setClassName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")
                }
                "huawei" -> Intent().apply {
                    setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")
                }
                "samsung" -> Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = android.net.Uri.parse("package:$packageName")
                }
                else -> {
                    Toast.makeText(this, "Please enable Auto-Start manually in device settings.", Toast.LENGTH_LONG).show()
                    return
                }
            }
            startActivity(intent)
            sharedPreferences.edit().putBoolean("auto_start_granted", true).apply()
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Auto-Start settings not found on this device.", Toast.LENGTH_LONG).show()
        }
    }

    private fun showPermissionDeniedMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
