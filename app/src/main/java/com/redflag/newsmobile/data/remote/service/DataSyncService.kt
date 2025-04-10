package com.redflag.newsmobile.data.remote.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.redflag.newsmobile.workers.DataSyncWorker
import java.util.concurrent.TimeUnit

class DataSyncService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val dataSyncWorkRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(5, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "DataSyncWork",
            ExistingPeriodicWorkPolicy.UPDATE,
            dataSyncWorkRequest
        )
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}