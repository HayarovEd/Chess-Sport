package bk.stavki.na.sport.app

import android.app.Application
import bk.stavki.na.sport.app.utils.ONESIGNAL_APP_ID
import com.onesignal.OneSignal

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}