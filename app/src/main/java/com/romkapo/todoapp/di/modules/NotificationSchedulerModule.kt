package com.romkapo.todoapp.di.modules

import com.romkapo.todoapp.utils.notificationmanager.NotificationScheduler
import com.romkapo.todoapp.utils.notificationmanager.NotificationSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface NotificationSchedulerModule {

    @Reusable
    @Binds
    fun bindNotificationModule(notificationsSchedulerImpl: NotificationSchedulerImpl): NotificationScheduler
}