package com.example.truecallercopy.receiver

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import com.example.truecallercopy.service.CallerOverlayService

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val number = intent?.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER, "-1")
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(object : PhoneStateListener() {

            override fun onCallStateChanged(state: Int, phoneNumber: String) {
                super.onCallStateChanged(state, phoneNumber)

                when (state) {
                    TelephonyManager.CALL_STATE_RINGING -> {
                        startCallerOverlayServiceIfNotRunning(context, phoneNumber)
                    }
                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                        Handler(Looper.getMainLooper()).postDelayed({
                            stopCallerOverlayService(context)
                        }, 5000000)
                    }
                    TelephonyManager.CALL_STATE_IDLE -> {
                        stopCallerOverlayService(context)
                    }
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun startCallerOverlayServiceIfNotRunning(context: Context, phoneNumber: String) {
        if (!isServiceRunning(context, CallerOverlayService::class.java)) {
            val popupIntent = Intent(context, CallerOverlayService::class.java).apply {
                putExtra("phoneNumber", phoneNumber)
            }
            ContextCompat.startForegroundService(context, popupIntent)
        }
    }

    private fun stopCallerOverlayService(context: Context) {
        val stopIntent = Intent(context, CallerOverlayService::class.java)
        context.stopService(stopIntent)
    }

    private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
