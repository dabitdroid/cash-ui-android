package cash.just.atm.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cash.just.atm.R
import cash.just.atm.model.BitcoinServer.getServer
import cash.just.atm.viewmodel.StatusViewModel
import cash.just.sdk.Cash
import cash.just.sdk.CashSDK
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    private lateinit var appContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appContext = view.context.applicationContext
        loginButton.setOnClickListener {
            CashSDK.login(getServer(), userPhoneNumber.text.toString(), object : Cash.WacCallback {
                override fun onSucceed() {
                    Toast.makeText(appContext, "on succeed", Toast.LENGTH_SHORT).show()
                }

                override fun onError(errorMessage: String?) {
                    Toast.makeText(appContext, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }

        confirmButton.setOnClickListener {
            CashSDK.loginConfirm(confirmLogin.text.toString(), object: Cash.WacCallback {
                override fun onSucceed() {
                    Toast.makeText(appContext, "on succeed", Toast.LENGTH_SHORT).show()
                }

                override fun onError(errorMessage: String?) {
                    Toast.makeText(appContext, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
