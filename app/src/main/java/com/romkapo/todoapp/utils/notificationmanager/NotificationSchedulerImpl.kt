package com.romkapo.todoapp.utils.notificationmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.data.model.network.AppSharedPreferences
import com.romkapo.todoapp.utils.Constants.TAG_NOTIFICATION_TASK
import javax.inject.Inject

class NotificationSchedulerImpl @Inject constructor(
    private val context: Context,
    private val sharedPreferences: AppSharedPreferences,
) : NotificationScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: TodoItem) {
        if (item.dateComplete != null) {
            if (
                item.dateComplete >= System.currentTimeMillis() &&
                !item.isComplete &&
                sharedPreferences.getNotificationStatus() == true
            ) {
                val intent = Intent(context, NotificationReceiver::class.java).apply {
                    putExtra(TAG_NOTIFICATION_TASK, item.toString())
                }
                sharedPreferences.addNotification(item.id.hashCode().toString())
                if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        alarmManager.canScheduleExactAlarms()
                    } else {
                        true
                    }
                ) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        item.dateComplete,
                        PendingIntent.getBroadcast(
                            context,
                            item.id.hashCode(),
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                        ),
                    )
                }
            }
        }
    }

    override fun cancel(item: TodoItem) {
        sharedPreferences.removeNotification(item.id.hashCode().toString())
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.id.hashCode(),
                Intent(context, NotificationReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )
    }

    override fun cancelAll() {
        for (task_id in sharedPreferences.getNotificationsIds().split(" ")) {
            sharedPreferences.removeNotification(task_id)
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    context,
                    task_id.hashCode(),
                    Intent(context, NotificationReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                ),
            )
        }
    }
}
