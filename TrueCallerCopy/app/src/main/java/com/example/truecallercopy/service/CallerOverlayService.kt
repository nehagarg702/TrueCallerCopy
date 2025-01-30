package com.example.truecallercopy.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.truecallercopy.data.model.CallerInfo
import com.example.truecallercopy.ui.view.CallerPopupScreen
import com.example.truecallercopy.ui.viewmodel.CallerViewModel
import org.koin.android.ext.android.inject


class CallerOverlayService : LifecycleService(), SavedStateRegistryOwner {

    private lateinit var windowManager: WindowManager
    private var overlayView: View? = null

    // Inject the ViewModel using Koin
    private val callerViewModel: CallerViewModel by inject()

    // SavedStateRegistryController to manage saved state
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performAttach()
        savedStateRegistryController.performRestore(null)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (!checkOverlayPermission()) {
            stopSelf()
            return START_NOT_STICKY
        }

        startForegroundService()

        val phoneNumber = intent?.getStringExtra("phoneNumber") ?: "Unknown Number"
        fetchCallerInfo(phoneNumber)

        return START_STICKY
    }

    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(this, "caller_overlay_channel")
            .setContentTitle("Caller Info Overlay")
            .setContentText("Displaying caller information")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .build()

        startForeground(1, notification)  // Regular notification, not an overlay
    }

    private fun fetchCallerInfo(phoneNumber: String) {
        callerViewModel.fetchCallerInfo(phoneNumber)

        // Observe the caller info LiveData
        callerViewModel.callerInfo.observe(this, Observer { callerInfo ->
            callerInfo?.let {
                showCallerOverlay(it)
            } ?: run {
                // Handle the case where callerInfo is null or an error occurred
                Toast.makeText(this, "Failed to fetch caller info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showCallerOverlay(callerInfo: CallerInfo) {
        if (overlayView != null) return
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@CallerOverlayService)
            setViewTreeSavedStateRegistryOwner(this@CallerOverlayService)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)

            // Add touch listener to handle touch events
            setOnTouchListener { v, event ->
                // If touch is inside the card bounds, handle it
                // Otherwise, pass it through to the background
                if(event.flags == MotionEvent.ACTION_DOWN) {
                    removeOverlay()
                    true
                }
                else {
                    false
                }
            }

            setContent {
                CallerPopupScreen(
                    callerInfo = callerInfo,
                    onClose = { removeOverlay() }
                )
            }
        }

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            0,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP
            y = 100 // Add some top margin
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        }

        overlayView = composeView
        windowManager.addView(composeView, layoutParams)
    }

    private fun removeOverlay() {
        overlayView?.let {
            windowManager.removeView(it)
            overlayView = null
            stopSelf()
        }
    }

    private fun checkOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(this)
    }

    private fun requestOverlayPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeOverlay()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "caller_overlay_channel",
                "Caller Overlay Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

}