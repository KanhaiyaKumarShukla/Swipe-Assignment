package com.example.swipeassignment.WorkManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.swipeassignment.repository.ProductRepository

class PostDataWorker(
    private val repository: ProductRepository,
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            repository.retryPendingPosts()
            Result.success()
        } catch (e: Exception) {
            Result.retry() // Retry if failed
        }
    }
}
