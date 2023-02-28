package bk.stavki.na.sport.app.ui.home

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.viewbinding.BuildConfig
import bk.stavki.na.sport.app.data.RemoteData
import bk.stavki.na.sport.app.ui.home.HomeState.Error
import bk.stavki.na.sport.app.ui.home.HomeState.Loading
import bk.stavki.na.sport.app.ui.home.HomeState.NoInternet
import bk.stavki.na.sport.app.ui.home.HomeState.SuccessConnect
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import java.util.*

class HomeViewModel : ViewModel() {

    private val _showData = MutableLiveData<HomeState>(Loading)
    val showData = _showData
    private val remoteConfig = Firebase.remoteConfig

    fun getFromLocal(pathUrl: String = "", checkedInternetConnection: Boolean) {
        if (pathUrl != "") {
            if (checkedInternetConnection) {
                _showData.value =
                    SuccessConnect(remoteData = RemoteData(pathUrl))
            } else {
                _showData.value = NoInternet("No Internet connection")
            }
        } else {
            if (checkedInternetConnection) {
                val configSettings = remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 3600
                }
                remoteConfig.setConfigSettingsAsync(configSettings)
                remoteConfig.fetchAndActivate()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val resultUrl = remoteConfig.getString("url")
                            if (checkIsEmu() || resultUrl == "") {
                                _showData.value =
                                    Error("Is device - emulator or empty Url")
                            } else {
                                _showData.value =
                                    SuccessConnect(RemoteData(urlPath = resultUrl))
                            }
                        } else {
                            Error(message = it.result.toString())
                        }
                    }.addOnFailureListener {
                        _showData.value =
                            Error(message = it.message ?: "Unknown error")
                    }
            } else {
                _showData.value = NoInternet("No Internet connection")
            }

        }
    }

    private fun checkIsEmu(): Boolean {
        if (BuildConfig.DEBUG) return false
        val phoneModel = Build.MODEL
        val buildProduct = Build.PRODUCT
        val buildHardware = Build.HARDWARE
        var result = (Build.FINGERPRINT.startsWith("generic")
                || phoneModel.contains("google_sdk")
                || phoneModel.lowercase(Locale.getDefault()).contains("droid4x")
                || phoneModel.contains("Emulator")
                || phoneModel.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || buildHardware == "goldfish"
                || Build.BRAND.contains("google")
                || buildHardware == "vbox86"
                || buildProduct == "sdk"
                || buildProduct == "google_sdk"
                || buildProduct == "sdk_x86"
                || buildProduct == "vbox86p"
                || Build.BOARD.lowercase(Locale.getDefault()).contains("nox")
                || Build.BOOTLOADER.lowercase(Locale.getDefault()).contains("nox")
                || buildHardware.lowercase(Locale.getDefault()).contains("nox")
                || buildProduct.lowercase(Locale.getDefault()).contains("nox"))
        if (result) return true
        result = result or (Build.BRAND.startsWith("generic") &&
                Build.DEVICE.startsWith("generic"))
        if (result) return true
        result = result or ("google_sdk" == buildProduct)
        return result
    }
}