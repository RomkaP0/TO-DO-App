package com.romkapo.todoapp

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.romkapo.todoapp.domain.network.UpdateLocalDataWorker
import com.romkapo.todoapp.domain.network.UpdateWorkerFactory
import com.romkapo.todoapp.di.components.app.AppComponent
import com.romkapo.todoapp.di.components.app.DaggerAppComponent
import com.romkapo.todoapp.utils.Constants.UPDATE_LOCAL_WORKER_NAME
import com.romkapo.todoapp.utils.Constants.UPDATE_PERIOD
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class App : Application() {
    private var _appComponent: AppComponent? = null
    val appComponent
        get() = requireNotNull(_appComponent) {
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

        WorkManager.initialize(this, setConfiguration())
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            UPDATE_LOCAL_WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            setWorkRequest(),
        )
    }

    private fun setWorkRequest() = PeriodicWorkRequestBuilder<UpdateLocalDataWorker>(
        UPDATE_PERIOD,
        TimeUnit.HOURS,
    ).setConstraints(setConstraints()).build()

    private fun setConfiguration() = Configuration.Builder()
        .setWorkerFactory(serviceFactory)
        .build()

    private fun setConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> (applicationContext as App).appComponent
    }
