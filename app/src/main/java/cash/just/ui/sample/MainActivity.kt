package cash.just.ui.sample

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import cash.just.atm.base.AtmResult
import cash.just.atm.base.ScanDataResult
import cash.just.atm.base.saveFunctions
import cash.just.atm.model.BitcoinServer
import cash.just.atm.utils.DriverLicenseUtil
import cash.just.sdk.AuthSharedPreferenceManager
import cash.just.sdk.Cash
import cash.just.sdk.CashSDK
import cash.just.sdk.model.KycDocType
import cash.just.sdk.model.WacBaseResponse
import cash.just.support.context
import cash.just.support.launchWebsite
import cash.just.ui.CashUI
import cash.just.ui.sample.FileUtil.getPath
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File


class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE_MAP = 0x01
        private const val REQUEST_CODE_LIST = 0x02
        private const val REQUEST_CODE_STATUS = 0x03
        private const val REQUEST_CODE_LOGIN = 0x04
        private const val REQUEST_CODE_SIGNUP = 0x05
        private const val REQUEST_CODE_SHOW_PROFILE = 0x06
        private const val REQUEST_CODE_SCAN_DOCUMENT = 0x07
        private const val REQUEST_ID_FRONT_UPLOAD = 0x08
        private const val REQUEST_ID_BACK_UPLOAD = 0x09
        private const val REQUEST_SELFIE_UPLOAD = 0x10
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

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        }

        Firebase.saveFunctions()?.let {
            getToken(it)
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

        login.setOnClickListener {
            CashUI.showLogin(this@MainActivity, REQUEST_CODE_LOGIN)
        }

        singup.setOnClickListener {
            CashUI.showSignUp(this@MainActivity, REQUEST_CODE_SIGNUP)
        }

        profile.setOnClickListener {
            var session: String? = null
            AuthSharedPreferenceManager.getSession(context)?.let {
                session = it
            }
            if(session == null) {
                Toast.makeText(context, "Please login to access profile page", Toast.LENGTH_SHORT).show()
            } else {
                val lifecycleOwner: LifecycleOwner = this
                lifecycleOwner.launchWebsite("https://cash-dev.coinsquareatm.com/external#key=${session}&mode=kyc")
            }
        }

        userStateButton.setOnClickListener {
            userState.text = CashSDK.getUserState(false).toString()
        }

        scanQrCode.setOnClickListener {
            CashUI.scanDocument(this@MainActivity, REQUEST_CODE_SCAN_DOCUMENT)
        }

        idFront.setOnClickListener {
            selectImage(this, REQUEST_ID_FRONT_UPLOAD)
        }

        idBack.setOnClickListener {
            selectImage(this, REQUEST_ID_BACK_UPLOAD)
        }

        idSelfie.setOnClickListener {
            selectImage(this, REQUEST_SELFIE_UPLOAD)
        }
    }

    private fun selectImage(context: Context, requestCode: Int) {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Choose your profile picture")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> {
                    val intent = Intent(ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, requestCode)
                }
                options[item] == "Choose from Gallery" -> {
                    val pickPhoto = Intent(Intent.ACTION_PICK)
                    pickPhoto.type = "image/*"
                    startActivityForResult(pickPhoto, requestCode)
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_MAP || requestCode == REQUEST_CODE_LIST || requestCode == REQUEST_CODE_STATUS || requestCode == REQUEST_CODE_SCAN_DOCUMENT) {
            when (resultCode) {
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(context, "Result Cancelled", Toast.LENGTH_SHORT).show()
                    consoleLog.setText(getString(R.string.result_cancelled))
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
                                AtmResult.SCAN -> {
                                    val resultData: ScanDataResult? = CashUI.getScanDetailsData(it)
                                    println(result)
                                    driverDetails.visibility = View.VISIBLE
                                    if (resultData != null) {
                                        driverDetails.text =
                                            "Driver Name: ${resultData.resultMaps.get(
                                                DriverLicenseUtil.FIRST_NAME)} ${resultData.resultMaps.get(
                                                DriverLicenseUtil.LAST_NAME
                                            )} " +
                                                    "\nLicense Number: ${resultData.resultMaps.get(
                                                        DriverLicenseUtil.LICENSE_NUMBER)} " +
                                                    "\nAddress: ${resultData.resultMaps.get(
                                                        DriverLicenseUtil.STREET)}"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if((requestCode == REQUEST_ID_FRONT_UPLOAD || requestCode == REQUEST_ID_BACK_UPLOAD || requestCode == REQUEST_SELFIE_UPLOAD) && resultCode == Activity.RESULT_OK) {
            val docType = when (requestCode) {
                REQUEST_ID_FRONT_UPLOAD -> KycDocType.ID_FRONT
                REQUEST_ID_BACK_UPLOAD -> KycDocType.ID_BACK
                REQUEST_SELFIE_UPLOAD -> KycDocType.SELFIE
                else -> throw Exception()
            }
            val selectedImageResult = if(data?.data != null) {
                data.data!!
            } else {
                data?.extras?.get("data") as Bitmap
            }
            val file = if(selectedImageResult is Bitmap) {
                selectedImage.setImageBitmap(selectedImageResult)
                File(getImageUri(this, selectedImageResult)?.let { getPath(it, this) })
            } else {
                selectedImage.setImageURI(selectedImageResult as Uri)
                File(getPath(selectedImageResult as Uri, this))
            }
            //val file = File(data.data!!.path)

            println(file)
            val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val filePartImage =
                MultipartBody.Part.createFormData("auto_upload_file", file.name, requestBody)
            var session: String? = null
            AuthSharedPreferenceManager.getSession(context)?.let {
                session = it
            }
            if (session == null) {
                Toast.makeText(context, "Please login to upload image page", Toast.LENGTH_SHORT)
                    .show()
            } else {
                CashSDK.uploadKycDocs(docType, filePartImage).enqueue(object :
                    Callback<WacBaseResponse> {
                    override fun onResponse(
                        call: Call<WacBaseResponse>,
                        response: Response<WacBaseResponse>
                    ) {
                        print(response.body())
                        if (response.isSuccessful) {
                            if (response.body()?.result?.toLowerCase() == "ok") {
                                Toast.makeText(context, "Image uploaded", Toast.LENGTH_SHORT)
                                    .show()
                                println("success")
                            }
                        }
                    }

                    override fun onFailure(call: Call<WacBaseResponse>, t: Throwable) {
                        Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT)
                            .show()
                        println(t.message)
                    }
                })
            }
        }
    }

    private fun getToken(functions: FirebaseFunctions) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.tag(TAG).w(task.exception, "Fetching FCM registration token failed")
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            registerToken(functions, token)
                .addOnCompleteListener(OnCompleteListener { registerTask ->
                    if (!registerTask.isSuccessful) {
                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                            Timber.tag(TAG).d("${code}:  $details")
                        }
                    } else {
                        Timber.tag(TAG).d("Task successful")
                    }
                })

            Timber.tag(TAG).d(token)
        })
    }

    private fun registerToken(functions: FirebaseFunctions, token: String?): Task<String>  {
        val packageVersion = getPackageVersion()
        val data = hashMapOf(
            "fcmToken" to token,
            "deviceId" to Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ANDROID_ID
            ),
            "phone" to "",
            "deviceModel" to Build.MODEL,
            "appVersion" to packageVersion,
        )
        return functions
            .getHttpsCallable("registerToken")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as String
                result
            }.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.tag(TAG).d("task unsuccessful: ${task.exception}")
                    val e = task.exception
                    if (e is FirebaseFunctionsException) {
                        val code = e.code
                        val details = e.details
                        Timber.tag(TAG).d("${code}: $details")
                    }
                } else {
                    Timber.tag(TAG).d("Task successfull")
                }
            })
    }

    private fun getPackageVersion(): Any {
        return try {
            val pInfo =
                context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e)
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

}