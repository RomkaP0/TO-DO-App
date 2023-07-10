package com.romkapo.todoapp.data.network

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject

class UpdateWorkerFactory @Inject constructor(
    private val updateLocalDataWorkerFactory: UpdateLocalDataWorker.Factory,
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            UpdateLocalDataWorker::class.java.name ->
                updateLocalDataWorkerFactory.create(appContext, workerParameters)
            else -> null
        }
    }
}
