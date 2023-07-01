package com.romkapo.todoapp.data.network

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.romkapo.todoapp.data.repository.MainRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateLocalDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: MainRepository
) : CoroutineWorker(context, workerParams) {
    //    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
//        return@withContext when (mergeData()) {
//            is Resource.Success<*> -> Result.success()
//            else -> {
//                Result.failure()
//            }
//        }
    override suspend fun doWork(): Result {
        return try {
            repository.getRemoteTasks()
            Result.success()
        }
        catch (e:Exception){
            Result.retry()
    }
}


//    private fun mergeData() = runBlocking {
//        return@runBlocking repository.getRemoteTasks()
//    }
}
