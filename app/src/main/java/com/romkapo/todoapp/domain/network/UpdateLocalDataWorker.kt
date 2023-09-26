package com.romkapo.todoapp.domain.network

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.romkapo.todoapp.data.local.Resource
import com.romkapo.todoapp.domain.repository.MainRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

// @HiltWorker
class UpdateLocalDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: MainRepository,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return if (repository.fetchTasks() is Resource.Success) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(appContext: Context, params: WorkerParameters): UpdateLocalDataWorker
    }
}
