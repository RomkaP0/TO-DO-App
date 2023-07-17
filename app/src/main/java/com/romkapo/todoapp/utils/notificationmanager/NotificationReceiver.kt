package com.romkapo.todoapp.utils.notificationmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.romkapo.todoapp.R
import com.romkapo.todoapp.appComponent
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.presentation.screen.main.MainActivity
import com.romkapo.todoapp.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var scheduler: NotificationSchedulerImpl

    @Inject
    lateinit var coroutineScope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        context.appComponent.injectNotificationReceiver(this)
        try {
            val gson = Gson()
            val task = gson.fromJson(
                intent.getStringExtra(Constants.TAG_NOTIFICATION_TASK),
                TodoItem::class.java
            )

            coroutineScope.launch(Dispatchers.IO) {

                val manager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                manager.createNotificationChannel(
                    NotificationChannel(
                        Constants.NOTIFICATION_CHANNEL_ID,
                        Constants.NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                )

                val notification =
                    NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_plus)
                        .setContentTitle(task.text)
                        .setContentText(
                            context.getString(
                                R.string.notification_text,
                                task.text,
                                task.importance.toString().lowercase(Locale.ROOT)
                            )
                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .addAction(
                            NotificationCompat.Action(
                                R.drawable.ic_arrow_right,
                                context.getString(R.string.postpone_for_a_day),
                                postponeIntent(context, task)
                            )
                        )
                        .setContentIntent(deepLinkIntent(context, task.id))
                        .build()

                scheduler.cancel(task)
                manager.notify(task.id.hashCode(), notification)
            }
        } catch (err: Exception) {
            Log.e(NotificationReceiver::class.java.simpleName, err.stackTraceToString())
        }
    }

    private fun deepLinkIntent(context: Context, newTaskArg: String): PendingIntent{
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("todoapp://add_edit/$newTaskArg"),
            context,
            MainActivity::class.java
        )
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }


    private fun postponeIntent(context: Context, item: TodoItem): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            item.id.hashCode(),
            Intent(
                context,
                NotificationPostponeReceiver::class.java
            ).apply {
                putExtra(Constants.TAG_NOTIFICATION_TASK, item.toString())
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
}
