package com.project0004ori

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
import com.project0004ori.ui.theme.Project0004oriTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception


const val REQUEST_CODE_SIGN_IN = 0
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         auth= FirebaseAuth.getInstance()

        setContent {
            Project0004oriTheme {
                // A surface container using the 'background' color from the theme
                MySignInScreen()

            }
        }
    }
    private fun googleAuthForFirebase(account: GoogleSignInAccount){

        val credentials =GoogleAuthProvider.getCredential(account.idToken,null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).await()
                withContext(Dispatchers.Main){
                    Toast.makeText(this@MainActivity,"Successfully Logged In",
                        Toast.LENGTH_LONG).show()

                }
            }catch (e :Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_LONG).show()
                }
            }
        }

    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE_SIGN_IN){
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let{
                googleAuthForFirebase(it)
            }
        }
    }

@Composable
fun MySignInScreen(){

    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
          Button(
              onClick = {
                  val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                      .requestIdToken("163936246170-rpak6j16mn8nnlp2l7h71gdbutqtnp6d.apps.googleusercontent.com")
                      .requestEmail()
                      .build()

                  val signInClient = GoogleSignIn.getClient(context,options)

                  signInClient.signInIntent.also {
                      startActivityForResult(it, REQUEST_CODE_SIGN_IN)
                  }

              }
          ){
              Text("Sign in")
          }
    }

}
}



