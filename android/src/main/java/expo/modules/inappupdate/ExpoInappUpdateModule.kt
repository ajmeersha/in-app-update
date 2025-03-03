package expo.modules.inappupdate

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK

import android.content.Context
import android.content.SharedPreferences
import androidx.core.os.bundleOf
import android.content.Intent;
import android.net.Uri
import android.util.Log;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.Promise
import expo.modules.kotlin.exception.CodedException
import expo.modules.kotlin.modules.ModuleDefinition
import java.net.URL

class ExpoInappUpdateModule : Module() {

  private lateinit var appUpdateManager: AppUpdateManager

  private val MY_REQUEST_CODE = 101
  private val IMMEDIATE_REQ_CODE = 200
  private val RESULT_CODE = 0


    private fun redirectToStore() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://play.google.com/store/apps/details?id=${appContext.reactContext?.packageName}")
            // setPackage("com.android.vending")
        }
        appContext.currentActivity?.startActivity(intent)
    }

      private val listener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            appUpdateManager.completeUpdate()
        } else if (state.installStatus() == InstallStatus.FAILED){
            redirectToStore()
        }
    }

  override fun definition() = ModuleDefinition {

    Name("ExpoInappUpdate")

     OnCreate {
            appUpdateManager = AppUpdateManagerFactory.create(appContext.reactContext!!)
            appUpdateManager.registerListener(listener)
        }

        OnDestroy {
            appUpdateManager.unregisterListener(listener)
        }

    Events("updateCancelled", "immediateUpdateCancelled")

    AsyncFunction("isUpdateAvailable") { promise: Promise -> 
      val appUpdateInfo = appUpdateManager.appUpdateInfo
      appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
          promise.resolve(true)
        } else {
          promise.resolve(false)
                    }
    }.addOnFailureListener { error ->
                promise.reject(CodedException("Failed to check for updates: ${error.message}", error))
            }
  }
    
    AsyncFunction("startUpdate") { isImmediate: Boolean, promise: Promise ->
      val appUpdateInfo = appUpdateManager.appUpdateInfo
      appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
        && isImmediate
        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
           appContext.currentActivity?.let { activity ->
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                            AppUpdateType.IMMEDIATE, activity, MY_REQUEST_CODE)

                        promise.resolve(true)
        }?: run {
                        promise.reject(CodedException("Current activity is null"))
                    }
      } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    appContext.currentActivity?.let { activity ->
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                            AppUpdateType.FLEXIBLE, activity, MY_REQUEST_CODE)

                        promise.resolve(true)
                    }?: run {
                        promise.reject(CodedException("Current activity is null"))
                    }
                } else {
                    promise.resolve(false)
                }
            }.addOnFailureListener { error ->
                promise.reject(CodedException("Failed to start update flow: ${error.message}", error))
            }
    }

      OnActivityResult { _, activityResult ->
              if (activityResult.requestCode == MY_REQUEST_CODE) {
                if (activityResult.resultCode == RESULT_CANCELED) {
                    this@ExpoInappUpdateModule.sendEvent("updateCancelled", mapOf(
        "resultCode" to MY_REQUEST_CODE
      ));
                }
            } else if(activityResult.requestCode == IMMEDIATE_REQ_CODE) {
                if(activityResult.resultCode != RESULT_OK) {
                    this@ExpoInappUpdateModule.sendEvent("immediateUpdateCancelled", mapOf(
        "resultCode" to activityResult.resultCode
      ));
                }
            }
        }
  } 
}
