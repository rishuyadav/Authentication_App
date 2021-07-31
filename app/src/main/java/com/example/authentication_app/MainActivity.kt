package com.example.authentication_app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.authentication_app.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        val userId= intent.getStringExtra("user_id")
        val EmailId= intent.getStringExtra("email_id")
        binding.uid.text="User Id:: $userId"
        binding.email.text="Email ID:: $EmailId"

        binding.logoutbutton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@MainActivity,LoginScreen::class.java))
            finish()
        }


    }
}



