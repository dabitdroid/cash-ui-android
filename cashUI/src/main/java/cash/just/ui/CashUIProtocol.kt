package cash.just.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentManager
import cash.just.atm.AtmResult
import cash.just.atm.DetailsDataResult
import cash.just.atm.SendDataResult
import cash.just.sdk.Cash
import cash.just.support.CashSupport

interface CashUIProtocol {
  fun init(network: Cash.BtcNetwork)
  fun startCashOutActivityForResult(activity: Activity, requestCode:Int)
  fun showStatusList(context: Context)
  fun getResult(intent:Intent): AtmResult?
  fun getSendData(intent:Intent): SendDataResult?
  fun getDetailsData(intent:Intent): DetailsDataResult?
  fun showSupportPage(builder: CashSupport.Builder, fragmentManager: FragmentManager)
}