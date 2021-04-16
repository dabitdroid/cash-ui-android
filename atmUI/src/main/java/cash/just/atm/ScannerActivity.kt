package cash.just.atm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cash.just.atm.base.AtmFlowActivity
import cash.just.atm.utils.DriverLicenseUtil
import cash.just.sdk.CashSDK
import cash.just.sdk.model.KycPiResponse
import cash.just.sdk.model.KycStatusResponse
import cash.just.sdk.model.WacBaseResponse
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.square.project.base.context
import kotlinx.android.synthetic.main.activity_scanner.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap

class ScannerActivity : AtmFlowActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        scanQR.setOnClickListener {
            val integrator = IntentIntegrator(this)

            integrator.setOrientationLocked(false)
            integrator.setPrompt("Scan QR code")
            integrator.setBeepEnabled(false) //Use this to set whether you need a beep sound when the QR code is scanned
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE, IntentIntegrator.PDF_417)
            integrator.initiateScan()
        }

        updateInfo.setOnClickListener {
            val firstName = firstName.text.toString()
            val lastName = lastName.text.toString()
            val ssn = ssn.text.toString()
            println("$firstName, $lastName, $ssn ")

            CashSDK.updatePersonalInformation(firstName, lastName, ssn).enqueue(object: Callback<WacBaseResponse>{
                override fun onResponse(call: Call<WacBaseResponse>, response: Response<WacBaseResponse>) {
                    if(response.isSuccessful) {
                        Toast.makeText(context, "Info updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Info update failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<WacBaseResponse>, t: Throwable) {
                    Toast.makeText(context, "Info Update Failed", Toast.LENGTH_SHORT).show()
                }
            })
        }

        kycInfo.setOnClickListener {
            CashSDK.getKycStatus().enqueue(object: Callback<KycStatusResponse>{
                override fun onResponse(call: Call<KycStatusResponse>, response: Response<KycStatusResponse>) {
                    if(response.isSuccessful) {
                        if(response.body()?.result?.toLowerCase() == "ok") {
                            kycDetails.visibility = View.VISIBLE
                            kycDetails.text = "Purchase Max: ${currencyFormat(response.body()!!.data.items[0].purchaseDailyAmountLimit)} \n\n" +
                                    "Daily Total Left \n${response.body()!!.data.items[0].purchaserRemainCount} of ${response.body()!!.data.items[0].pur_daily_count_limit} purchases" +
                                    "\n${currencyFormat(response.body()!!.data.items[0].purchaseRemainAmount)} of ${currencyFormat(response.body()!!.data.items[0].purchaseDailyAmountLimit)}"
                        }
                    } else {
                        Toast.makeText(context, "Info update failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<KycStatusResponse>, t: Throwable) {
                    Toast.makeText(context, "Info Update Failed", Toast.LENGTH_SHORT).show()
                }
            })

        }

        profileInfo.setOnClickListener {
            CashSDK.getKycPersonalInformation().enqueue(object: Callback<KycPiResponse>{
                override fun onResponse(call: Call<KycPiResponse>, response: Response<KycPiResponse>) {
                    if(response.isSuccessful) {
                        if(response.body()?.result?.toLowerCase() == "ok") {
                            profileDetails.visibility = View.VISIBLE
                            profileDetails.text = "First Name: ${response.body()!!.data.items[0].firstName} \n" +
                                    "Last Name: ${response.body()!!.data.items[0].lastName}" +
                                    "\nSSN: ${response.body()!!.data.items[0].ssn}"
                        }
                    } else {
                        Toast.makeText(context, "Info update failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<KycPiResponse>, t: Throwable) {
                    Toast.makeText(context, "Info Update Failed", Toast.LENGTH_SHORT).show()
                }
            })

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(applicationContext, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                if(result.formatName == IntentIntegrator.PDF_417) {
                    val resultMaps: HashMap<String, String>? = DriverLicenseUtil.readDriverLicense(result.contents)
                    resultMaps?.let { this.onScanResult(it) }
                    if (resultMaps != null) {
                        driverDetails.visibility = View.VISIBLE
                        driverDetails.text =
                                    "Driver Name: ${resultMaps[DriverLicenseUtil.FIRST_NAME]} ${resultMaps[DriverLicenseUtil.LAST_NAME]} " +
                                    "\nLicense Number: ${resultMaps[DriverLicenseUtil.LICENSE_NUMBER]} " +
                                    "\nAddress: ${resultMaps[DriverLicenseUtil.STREET]}"

                        firstName.setText(resultMaps[DriverLicenseUtil.FIRST_NAME])
                        lastName.setText(resultMaps[DriverLicenseUtil.LAST_NAME])
                    }
                }
                println(result.contents)
            }
        }
        //finish()
    }

    fun currencyFormat(amount: String): String? {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("USD")
        return format.format(amount.toDouble())
    }
}