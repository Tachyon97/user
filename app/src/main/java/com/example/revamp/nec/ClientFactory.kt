package com.example.revamp.nec

import android.content.Context
import com.amazonaws.mobile.auth.core.IdentityManager
import com.amazonaws.mobile.auth.userpools.CognitoUserPoolsSignInProvider
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.appsync.sigv4.BasicCognitoUserPoolsAuthProvider

// Supply AppSync to perform data access
object ClientFactory {
    @Volatile
    private var client: AWSAppSyncClient? = null

    @Synchronized
    fun init(context: Context?) {
        if (client == null) {
            val awsConfiguration = AWSConfiguration(context)
            val cognitoUserPoolsSignInProvider =
                IdentityManager.getDefaultIdentityManager().currentIdentityProvider as CognitoUserPoolsSignInProvider
            val basicCognitoUserPoolsAuthProvider =
                BasicCognitoUserPoolsAuthProvider(cognitoUserPoolsSignInProvider.cognitoUserPool)
            client = AWSAppSyncClient.builder()
                .context(context!!)
                .awsConfiguration(awsConfiguration)
                .cognitoUserPoolsAuthProvider(basicCognitoUserPoolsAuthProvider)
                .build()
        }
    }

    @Synchronized
    fun appSyncClient(): AWSAppSyncClient? {
        return client
    }
}