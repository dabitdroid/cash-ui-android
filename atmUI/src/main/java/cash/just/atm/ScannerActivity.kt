package cash.just.atm

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import cash.just.atm.base.AtmFlowActivity
import cash.just.atm.utils.DriverLicenseUtil
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class ScannerActivity : AtmFlowActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        val integrator = IntentIntegrator(this)

        integrator.setOrientationLocked(false)
        integrator.setPrompt("Scan QR code")
        integrator.setBeepEnabled(false) //Use this to set whether you need a beep sound when the QR code is scanned
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE, IntentIntegrator.PDF_417)
        integrator.initiateScan()
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
                }
                println(result.contents)
            }
        }
        finish()
    }
}