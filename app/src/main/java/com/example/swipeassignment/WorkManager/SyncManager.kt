package com.example.swipeassignment.WorkManager

import android.content.Context
import androidx.work.*

object SyncManager {
    fun scheduleSync(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Only run when internet is available
            .build()

        val workRequest = OneTimeWorkRequestBuilder<PostDataWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}
