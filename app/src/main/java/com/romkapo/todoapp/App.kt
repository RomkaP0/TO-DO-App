package com.romkapo.todoapp

import android.app.Application
import android.content.Context
import com.romkapo.todoapp.core.components.AppComponent
import com.romkapo.todoapp.core.components.DaggerAppComponent

//class App : Application(), Configuration.Provider {
class App : Application(){
    private var _appComponent: AppComponent? = null
    val appComponent get() = requireNotNull(_appComponent){
        "AppComponent must not be null"
    }
    //
//    @Inject
//    lateinit var workerFactory: HiltWorkerFactory
//
//    override fun getWorkManagerConfiguration() =
//        Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .build()

    override fun onCreate() {
        super.onCreate()

        _appComponent = DaggerAppComponent.factory().create(this)


//        val updateInterval = UPDATE_PERIOD // hours
//
//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()
//
//        val workRequest = PeriodicWorkRequestBuilder<UpdateLocalDataWorker>(
//            updateInterval,
//            TimeUnit.HOURS
//        ).setConstraints(constraints).build()
//
//        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
//            UPDATE_LOCAL_WORKER_NAME,
//            ExistingPeriodicWorkPolicy.KEEP,
//            workRequest
//        )
    }
}
val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> (applicationContext as App).appComponent
    }
