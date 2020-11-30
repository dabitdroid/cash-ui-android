package cash.just.ui.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cash.just.atm.base.AtmResult
import cash.just.atm.model.BitcoinServer
import cash.just.sdk.Cash
import cash.just.support.*
import cash.just.support.pages.SupportPagesLoader
import cash.just.support.pages.Topic
import cash.just.ui.CashUI
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE_MAP = 0x01
        private const val REQUEST_CODE_LIST = 0x02
        private const val REQUEST_CODE_STATUS = 0x03
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BitcoinServer.setServer(Cash.BtcNetwork.TEST_NET)
        CashUI.init(Cash.BtcNetwork.TEST_NET)
        serverToggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                CashUI.init(Cash.BtcNetwork.TEST_NET)
                BitcoinServer.setServer(Cash.BtcNetwork.TEST_NET)
            } else {
                CashUI.init(Cash.BtcNetwork.MAIN_NET)
                BitcoinServer.setServer(Cash.BtcNetwork.MAIN_NET)
            }
        }

        openMaps.setOnClickListener {
            CashUI.startCashOutActivityForResult(this@MainActivity, REQUEST_CODE_MAP)
        }

        openActivity.setOnClickListener {
            CashUI.showStatusList(this@MainActivity, REQUEST_CODE_LIST)
        }

        openStatus.setOnClickListener {
            CashUI.showStatus(this@MainActivity, cashCode.text.toString(), REQUEST_CODE_STATUS)
        }

        openSupport.setOnClickListener {
            startActivity(Intent(this, SupportActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_MAP || requestCode == REQUEST_CODE_LIST || requestCode == REQUEST_CODE_STATUS) {
            when (resultCode) {
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(context, "Result Cancelled", Toast.LENGTH_SHORT).show()
                    consoleLog.setText("Result Cancelled")
                }
                Activity.RESULT_OK -> {
                    Toast.makeText(context, "Result Ok", Toast.LENGTH_SHORT).show()
                    data?.let {
                        CashUI.getResult(it)?.let { result ->
                            when (result) {
                                AtmResult.SEND -> {
                                    val send = CashUI.getSendData(it)
                                    consoleLog.setText("Result OK \n" + send.toString())
                                }
                                AtmResult.DETAILS -> {
                                    val details = CashUI.getDetailsData(it)
                                    consoleLog.setText("Result OK \n" + details.toString())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
