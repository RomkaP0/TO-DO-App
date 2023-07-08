package com.romkapo.todoapp

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.romkapo.todoapp.core.components.app.AppComponent
import com.romkapo.todoapp.core.components.app.DaggerAppComponent
import com.romkapo.todoapp.data.network.UpdateLocalDataWorker
import com.romkapo.todoapp.data.network.UpdateWorkerFactory
import com.romkapo.todoapp.utils.Constants.UPDATE_LOCAL_WORKER_NAME
import com.romkapo.todoapp.utils.Constants.UPDATE_PERIOD
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class App : Application() {
    private var _appComponent: AppComponent? = null
    val appComponent get() = requireNotNull(_appComponent) {
        "AppComponent must not be null"
    }

    @Inject
    lateinit var serviceFactory: UpdateWorkerFactory

    override fun onCreate() {
        _appComponent = DaggerAppComponent.builder()
            .context(applicationContext)
            .build()

        appComponent.inject(this)

        super.onCreate()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<UpdateLocalDataWorker>(
            UPDATE_PERIOD,
            TimeUnit.HOURS,
        ).setConstraints(constraints).build()

        val configuration = Configuration.Builder()
            .setWorkerFactory(serviceFactory)
            .build()

        WorkManager.initialize(this, configuration)
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            UPDATE_LOCAL_WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest,
        )
    }
}
val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> (applicationContext as App).appComponent
    }
