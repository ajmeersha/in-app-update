package expo.modules.inappupdate

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK

import android.content.Context
import android.content.SharedPreferences
import androidx.core.os.bundleOf
import android.util.Log;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.Promise
import expo.modules.kotlin.modules.ModuleDefinition
import java.net.URL

class ExpoInappUpdateModule : Module() {

  private val currentActivity
  get() = appContext.throwingActivity

  // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  override fun definition() = ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
    // The module will be accessible from `requireNativeModule('ExpoInappUpdate')` in JavaScript.
    // val reactContext = appContext.reactContext
    // Log.i("ExpoInappUpdate","${reactContext.packageName}")
     val STALE_DAYS = 30
     val MY_REQUEST_CODE = 101
     val IMMEDIATE_REQ_CODE = 200
     var RESULT_CODE: Int = 0
     val UPDATE_ACCEPTED = "UPDATE_ACCEPTED"

    Name("ExpoInappUpdate")

    // Sets constant properties on the module. Can take a dictionary or a closure that returns a dictionary.
    // Constants(
    //   "PI" to Math.PI
    // )

    // Defines event names that the module can send to JavaScript.
    Events("onChange", "updateCancelled", "immediateUpdateCancelled")

    Function("getTheme") {
      return@Function getPreferences().getString("theme", "system")
    }

    Function("setTheme") { theme: String ->
      // Send an event to JavaScript.
      getPreferences().edit().putString("theme", theme).commit()
      this@ExpoInappUpdateModule.sendEvent("onChange", bundleOf(
        "theme" to theme
      ))
    }

    AsyncFunction("isUpdateAvailable") { promise: Promise -> 
      val appUpdateManager = AppUpdateManagerFactory.create(context)
      val appUpdateInfo = appUpdateManager.appUpdateInfo
      appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
        Log.i("ExpoInappUpdate", "Update availability: ${UpdateAvailability.UPDATE_AVAILABLE}")
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
          promise.resolve(true)
        } else {
          promise.resolve(false)
                    }
    }
  }
    
    AsyncFunction("checkForUpdate") { isImmediate: Boolean, promise: Promise ->
      val appUpdateManager = AppUpdateManagerFactory.create(context)
      val appUpdateInfo = appUpdateManager.appUpdateInfo
      appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
        Log.i("ExpoInappUpdate", "Update availability: ${appUpdateInfo.updateAvailability()}")
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
        && isImmediate
        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
            appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            AppUpdateType.IMMEDIATE,
            currentActivity,
            MY_REQUEST_CODE
            )
            // currentActivity.startActivityForResult(registerForActivityResult, MY_REQUEST_CODE)
            // currentActivity.startActivityForResult(updateIntent, MY_REQUEST_CODE)
          // appContext.throwingActivity.startActivityForResult(registerForActivityResult, MY_REQUEST_CODE)
        }
      }
    }


  // OnActivityResult { _, (requestCode, resultCode, intent) ->
  //   if (requestCode == MY_REQUEST_CODE) {
  //     if (resultCode == RESULT_CANCELED) {
  //         this@ExpoInappUpdateModule.sendEvent("updateCancelled", mapOf("result" to resultCode));
  //     }
  // } else if(requestCode == IMMEDIATE_REQ_CODE) {
  //     if(resultCode != RESULT_OK) {
  //         this@ExpoInappUpdateModule.sendEvent("immediateUpdateCancelled", mapOf("result" to resultCode));
  //     }
  // }
  // }

    // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
    // Function("hello") {
    //   "Hello world! 👋"
    // }

    // Defines a JavaScript function that always returns a Promise and whose native code
    // is by default dispatched on the different thread than the JavaScript runtime runs on.
    // AsyncFunction("setValueAsync") { value: String ->
    //   // Send an event to JavaScript.
    //   sendEvent("onChange", mapOf(
    //     "value" to value
    //   ))
    // }

    // Enables the module to be used as a native view. Definition components that are accepted as part of
    // the view definition: Prop, Events.
    // View(ExpoInappUpdateView::class) {
    //   // Defines a setter for the `url` prop.
    //   Prop("url") { view: ExpoInappUpdateView, url: URL ->
    //     view.webView.loadUrl(url.toString())
    //   }
    //   // Defines an event that the view can send to JavaScript.
    //   Events("onLoad")
    // }
  }  

  val context : Context
  get() = requireNotNull(appContext.reactContext)

  private fun getPreferences(): SharedPreferences {
    return context.getSharedPreferences(context.packageName + ".inapp-update", Context.MODE_PRIVATE)
  }
}
