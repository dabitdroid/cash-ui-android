package cash.just.ui

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask

import androidx.fragment.app.FragmentManager
import cash.just.atm.*
import android.content.Context
import cash.just.atm.base.*
import cash.just.sdk.AuthSharedPreferenceManager
import cash.just.sdk.Cash
import cash.just.sdk.CashSDK
import cash.just.support.CashSupport
import com.google.zxing.integration.android.IntentIntegrator
import timber.log.Timber

class CashUserInterfaceImpl : CashUIProtocol {
  override fun init(network: Cash.BtcNetwork) {
    //TODO: If session has been stored in the sharedPreference, session should be validated and userState should be refreshed.
    /*val let = AuthSharedPreferenceManager.getSession(context)?.let {
      AsyncTask.execute {
        CashSDK.getUserState(true)
      }
    }*/

    CashSDK.createGuestSession(network, object:Cash.SessionCallback {
      override fun onSessionCreated(sessionKey: String) {
        Timber.d("Session created")
      }
      override fun onError(errorMessage: String?) {
        Timber.e("Failed to create session %s", errorMessage)
      }
    })
  }

  override fun startCashOutActivityForResult(activity: Activity, requestCode:Int) {
    val intent = Intent(activity, AtmActivity::class.java)
    activity.startActivityForResult(intent, requestCode)
  }

  override fun showStatusList(activity: Activity, requestCode:Int) {
    activity.startActivityForResult(Intent(activity, StatusActivity::class.java), requestCode)
  }

  override fun showStatus(activity: Activity, code: String, requestCode:Int) {
    val intent = StatusActivity.newCashStatusIntent(activity, code)
    activity.startActivityForResult(intent, requestCode)
  }

  override fun getResult(intent:Intent): AtmResult? {
    return AtmFlowActivity.getResult(intent)
  }

  override fun getSendData(intent:Intent): SendDataResult? {
    return AtmFlowActivity.getSendData(intent)
  }

  override fun getDetailsData(intent:Intent): DetailsDataResult? {
    return AtmFlowActivity.getDetailsData(intent)
  }

  override fun getScanDetailsData(intent:Intent): ScanDataResult? {
    return AtmFlowActivity.getScanDetailsData(intent)
  }

  override fun showSupportPage(builder: CashSupport.Builder, fragmentManager: FragmentManager) {
    builder.build().createDialogFragment().show(fragmentManager, "supportPage")
  }

  override fun showLogin(activity: Activity, requestCode:Int) {
    activity.startActivityForResult(Intent(activity, LoginActivity::class.java), requestCode)
  }

  override fun showSignUp(activity: Activity, requestCode:Int) {
    activity.startActivityForResult(Intent(activity, SignUpActivity::class.java), requestCode)
  }

  override fun showProfile(activity: Activity, requestCode: Int) {
    activity.startActivity(Intent(activity, ShowProfileActivity::class.java))
  }

  override fun scanDocument(activity: Activity, requestCode: Int) {
    val intent = Intent(activity, ScannerActivity::class.java)
    activity.startActivityForResult(intent, requestCode)
  }
}