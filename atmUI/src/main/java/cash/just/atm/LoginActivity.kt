package cash.just.atm

import android.os.Bundle
import cash.just.atm.base.AtmFlowActivity

class LoginActivity : AtmFlowActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}