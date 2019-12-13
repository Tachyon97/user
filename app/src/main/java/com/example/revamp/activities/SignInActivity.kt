package com.example.revamp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.mobile.client.*
import com.example.revamp.R


class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        AWSMobileClient.getInstance()
            .initialize(applicationContext, object : Callback<UserStateDetails> {
                override fun onResult(result: UserStateDetails) {
                    when (result.userState) {
                        UserState.SIGNED_IN -> runOnUiThread {
                            val i = Intent(this@SignInActivity, MainActivity::class.java)
                            startActivity(i)
                        }
                        UserState.SIGNED_OUT -> runOnUiThread {
                            DisplaySignIn()
                        }
                        else -> runOnUiThread {
                            AWSMobileClient.getInstance().signOut()
                            DisplaySignIn()
                        }
                    }
                }

                override fun onError(e: Exception?) {
                    Log.d("@@@", "" + e.toString())
                }

            })
    }

    fun DisplaySignIn() {
        try {
            AWSMobileClient.getInstance().showSignIn(
                this,
                SignInUIOptions.builder().nextActivity(MainActivity::class.java).build()
            )
        } catch (exception: Exception) {
            Log.d("@@@", "" + exception)
        }
    }
}
