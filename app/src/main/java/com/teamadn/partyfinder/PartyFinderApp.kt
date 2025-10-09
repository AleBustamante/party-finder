package com.teamadn.partyfinder

import android.app.Application
import com.teamadn.partyfinder.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PartyFinderApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin activity
            androidLogger()
            // Pass Android context to Koin
            androidContext(this@PartyFinderApp)
            // Load your modules
            modules(appModule)
        }
    }
}