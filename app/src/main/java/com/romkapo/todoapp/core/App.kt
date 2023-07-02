package com.romkapo.todoapp.core

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.romkapo.todoapp.data.network.UpdateLocalDataWorker
import com.romkapo.todoapp.utils.Constants.UPDATE_LOCAL_WORKER_NAME
import com.romkapo.todoapp.utils.Constants.UPDATE_PERIOD
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp

class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        val updateInterval = UPDATE_PERIOD // hours

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<UpdateLocalDataWorker>(
            updateInterval,
            TimeUnit.HOURS
        ).setConstraints(constraints).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            UPDATE_LOCAL_WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
