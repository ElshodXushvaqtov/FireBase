package com.example.firebase

import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.firebase.ui.theme.FireBaseTheme
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var oneTapClient: SignInClient
        lateinit var signInRequest: BeginSignInRequest
        super.onCreate(savedInstanceState)
        setContent {
            FireBaseTheme {
                val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
                var showOneTapUI = true
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    oneTapClient = Identity.getSignInClient(this)
                    signInRequest = BeginSignInRequest.builder()
                        .setPasswordRequestOptions(
                            BeginSignInRequest.PasswordRequestOptions.builder()
                                .setSupported(true)
                                .build()
                        )
                        .setGoogleIdTokenRequestOptions(
                            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                // Your server's client ID, not your Android client ID.
                                .setServerClientId("fir-c8dd7")
                                // Only show accounts previously used to sign in.
                                .setFilterByAuthorizedAccounts(true)
                                .build()
                        )
                        // Automatically sign in when exactly one credential is retrieved.
                        .setAutoSelectEnabled(true)
                        .build()

                    oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener(this) { result ->
                            try {
                                startIntentSenderForResult(
                                    result.pendingIntent.intentSender, REQ_ONE_TAP,
                                    null, 0, 0, 0, null
                                )
                            } catch (e: IntentSender.SendIntentException) {
                                Log.e("AAA", "Couldn't start One Tap UI: ${e.localizedMessage}")
                            }
                        }
                        .addOnFailureListener(this) { e ->
                            // No saved credentials found. Launch the One Tap sign-up flow, or
                            // do nothing and continue presenting the signed-out UI.
                            e.localizedMessage?.let { Log.d("BBB", it) }
                        }
                }


            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FireBaseTheme {
        Greeting("Android")
    }
}