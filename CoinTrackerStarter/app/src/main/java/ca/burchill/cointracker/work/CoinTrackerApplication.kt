package ca.burchill.cointracker.work

import android.app.Application

import android.os.Build
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/*
 *  Editor: Minh Hang
 *  Description: Android Dev - Coin Tracker Application
 *  Date: 2021-04-11
 *  Note: based on Android Codelab 9.2
 */

class CoinTrackerApplication : Application() {
    // TODO - implement delayedInit() that ensures workmanager is refreshing the database regularly

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    @Suppress("DEPRECATION")
    private fun delayedInit() {
        applicationScope.launch {
            Timber.plant(Timber.DebugTree()) //used for debugging, will log messages to the Logcat Console
            setupRecurringWork()
        }
    }
    private fun setupRecurringWork() {
        // Edit these constraints as needed
        // Changed default behaviour to not run on low battery as this app is not designed to be critical.
        // Implementing a toggle option on the UI for low battery options would a great alternative to setting this here.
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<Refresh>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        Timber.d("Periodic Work request for sync is scheduled")

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            Refresh.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }


}