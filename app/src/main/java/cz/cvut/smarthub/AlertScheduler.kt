package cz.cvut.smarthub

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import cz.cvut.smarthub.database.Alert
import cz.cvut.smarthub.ui.screens.alerts.AlertViewModel

object AlertScheduler {
    fun scheduleAlerts(context: Context, alertViewModel: AlertViewModel) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(context, NotificationPublisher::class.java)

        // Alert every minute
        val interval = 60 * 1000L
        val triggerTime = System.currentTimeMillis() + interval

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            interval,
            pendingIntent
        )

        val alert = Alert(
            title = "New Alert",
            message = "This is a simulated alert.",
            timestamp = System.currentTimeMillis()
        )
        alertViewModel.insertAlert(alert)
    }
}