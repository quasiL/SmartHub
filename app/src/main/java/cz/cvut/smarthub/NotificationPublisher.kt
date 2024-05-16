package cz.cvut.smarthub

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class NotificationPublisher : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val alertTitle = intent.getStringExtra("alertTitle") ?: "New Alert"
        val alertMessage = intent.getStringExtra("alertMessage") ?: "You have a new alert."

        context.showNotification(alertTitle, alertMessage)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.showNotification(title: String, message: String) {
    val channel = NotificationChannel(
        "alert_channel",
        "Alert Channel",
        NotificationManager.IMPORTANCE_HIGH
    )
    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.createNotificationChannel(channel)

    val notification = NotificationCompat.Builder(this, "alert_channel")
        .setSmallIcon(R.drawable.baseline_add_alert_24)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .build()

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
}