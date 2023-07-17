package com.romkapo.todoapp.utils.notificationmanager

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.romkapo.todoapp.appComponent
import com.romkapo.todoapp.data.model.TodoItem
import com.romkapo.todoapp.domain.MainRepository
import com.romkapo.todoapp.utils.Constants.ONE_DAY_IN_MILLIS
import com.romkapo.todoapp.utils.Constants.TAG_NOTIFICATION_TASK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationPostponeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var coroutineScope: CoroutineScope

    @Inject
    lateinit var repository: MainRepository

    override fun onReceive(context: Context, intent: Intent) {
        context.appComponent.injectNotificationPostponeReceiver(this)
        val gson = Gson()
        val item = gson.fromJson(intent.getStringExtra(TAG_NOTIFICATION_TASK), TodoItem::class.java)
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(item.id.hashCode())
        try {
            coroutineScope.launch(Dispatchers.IO) {
                if (item.dateComplete != null) {
                    repository.updateTodoItem(item.copy(dateComplete = item.dateComplete + ONE_DAY_IN_MILLIS))
                }
            }
        } catch (err: Exception) {
            Log.e(NotificationPostponeReceiver::class.java.simpleName, err.message.toString())
        }
    }
}