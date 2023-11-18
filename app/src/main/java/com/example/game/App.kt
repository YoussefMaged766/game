package com.example.game

import android.app.Application
import android.content.res.Configuration
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class App :Application() {
    override fun onCreate() {
        super.onCreate()


    }
//     fun updateLocale() {
//        val systemLocale = resources.configuration.locales[0] // Get the preferred system locale
//        val locale = systemLocale
//
//        val config = Configuration()
//        config.setLocale(locale)
//
//        val context = createConfigurationContext(config)
//        resources.updateConfiguration(config, resources.displayMetrics)
//
//        // You can also set the locale for the whole app if needed
//         Locale.setDefault(locale)
//    }


}