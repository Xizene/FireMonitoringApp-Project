package com.example.firemonitoringapp

import android.app.*
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.firebase.database.*

class FireMonitoringService : Service() {

    private lateinit var databaseReference: DatabaseReference
    private var lastStatus = ""

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        // Service harus jadi foreground supaya tidak dimatikan sistem
        val notification = NotificationCompat.Builder(this, "service_channel")
            .setContentTitle("Fire Monitoring Aktif")
            .setContentText("Sistem monitoring berjalan")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()

        startForeground(100, notification)

        // Listener Firebase
        databaseReference = FirebaseDatabase.getInstance()
            .getReference("monitoring/status")

        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val status = snapshot.value.toString()

                if (status == "KEBAKARAN" && lastStatus != "KEBAKARAN") {
                    showFireNotification()
                }

                lastStatus = status
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun showFireNotification() {

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val builder = NotificationCompat.Builder(this, "fire_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("PERINGATAN!")
            .setContentText("Kebakaran Terdeteksi!")
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(1, builder.build())
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val fireChannel = NotificationChannel(
                "fire_channel",
                "Fire Alert",
                NotificationManager.IMPORTANCE_HIGH
            )

            val serviceChannel = NotificationChannel(
                "service_channel",
                "Monitoring Service",
                NotificationManager.IMPORTANCE_LOW
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(fireChannel)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}