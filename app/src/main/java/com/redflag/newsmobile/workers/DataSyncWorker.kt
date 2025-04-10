package com.redflag.newsmobile.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.redflag.newsmobile.data.remote.api.NewsAppApi
import com.redflag.newsmobile.data.remote.service.RetrofitHelper
import com.redflag.newsmobile.ui.view.activities.HomeActivity
import java.time.Instant

class DataSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val CHANNEL_ID = "news_updates_channel"
        const val NOTIFICATION_ID = 1
    }
    private val newsAppApi: NewsAppApi = RetrofitHelper().getInstance().create(NewsAppApi::class.java)

    override suspend fun doWork(): Result {
        return try {
            val newNewsCount = checkForNewNews()
            if (newNewsCount > 0) {
                showNotification(newNewsCount)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private suspend fun checkForNewNews(): Int {
        val response = newsAppApi.getHeadLines()
        if (response.isSuccessful) {
            val articles = response.body()?.articles ?: emptyList()
            if (articles.isEmpty()) return 0


            val prefs = applicationContext.getSharedPreferences("news_prefs", Context.MODE_PRIVATE)
            val lastTimestamp = prefs.getLong("last_fetched_timestamp", 0L)

            val newArticles = articles.filter { article ->
                try {
                    val publishedTimestamp = Instant.parse(article.publishedAt).toEpochMilli()
                    publishedTimestamp > lastTimestamp
                } catch (e: Exception) {
                    false
                }
            }

            try {
                val mostRecentTimestamp = Instant.parse(articles.first().publishedAt).toEpochMilli()
                prefs.edit().putLong("last_fetched_timestamp", mostRecentTimestamp).apply()
            } catch (e: Exception) {
            }

            return newArticles.size
        }
        return 0
    }

    private fun showNotification(newNewsCount: Int) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Atualizações de Notícias",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(applicationContext, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (PendingIntent.FLAG_IMMUTABLE)
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Novas Notícias")
            .setContentText("Há $newNewsCount novas notícias. Clique para atualizar.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}