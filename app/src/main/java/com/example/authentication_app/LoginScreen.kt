package com.example.authentication_app

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.authentication_app.databinding.ActivityLoginScreenBinding
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginScreen : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code:Int=123
    var firebaseAuth= FirebaseAuth.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var binding: ActivityLoginScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
// getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)
// initialize the firebaseAuth variable
        firebaseAuth= FirebaseAuth.getInstance()
        binding = ActivityLoginScreenBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.logintoregister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.login.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.emailLog.text.toString().trim() { it <= ' ' }) -> {
                    Toast.makeText(this@LoginScreen, "Please Enter Email.", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(binding.inputpass.text.toString().trim() { it <= ' ' }) -> {
                    Toast.makeText(this@LoginScreen, "Please Enter Password.", Toast.LENGTH_SHORT).show()
                }
                else->{
                    val email: String= binding.emailLog.text.toString().trim() { it <= ' ' }
                    val password: String=binding.inputpass.text.toString().trim() { it <= ' ' }
                    signInGoogle()
                }
                }
            }





                }

    private  fun signInGoogle(){

        val signInIntent:Intent=mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code){
            val task:Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e:ApiException){
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount){
        val email: String= binding.emailLog.text.toString().trim() { it <= ' ' }
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                Toast.makeText(this@LoginScreen,"You are Logged in Successfully",Toast.LENGTH_LONG).show()

                                    val intent=
                                            Intent(this@LoginScreen,MainActivity::class.java)
                                    intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK  or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id",FirebaseAuth.getInstance().currentUser?.uid)
                                    intent.putExtra("email_id",email)
                                    startActivity(intent)
                                    finish()
            }
            else{
                                   Toast.makeText(this@LoginScreen,task.exception?.message.toString(),Toast.LENGTH_SHORT).show()
                                }
        }
    }
    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
