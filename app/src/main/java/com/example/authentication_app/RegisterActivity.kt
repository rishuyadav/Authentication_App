package com.example.authentication_app

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.authentication_app.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.registertologin.setOnClickListener {
            startActivity(Intent(this,LoginScreen::class.java))
        }



        binding.buttonRegister.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.emailReg.text.toString().trim() { it <= ' ' }) -> {
                    Toast.makeText(this@RegisterActivity, "Please Enter Email.", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(binding.inputpass.text.toString().trim() { it <= ' ' }) -> {
                    Toast.makeText(this@RegisterActivity, "Please Enter Password.", Toast.LENGTH_SHORT).show()


                }
                else->{
                    val email: String= binding.emailReg.text.toString().trim() { it <= ' ' }
                    val password: String=binding.inputpass.text.toString().trim() { it <= ' ' }
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener { task->
                                if(task.isSuccessful){
                                    val firebaseUser: FirebaseUser? = task.result?.user
                                    Toast.makeText(this@RegisterActivity,"You are registered Successfully",Toast.LENGTH_SHORT).show()
                                    val intent= Intent(this@RegisterActivity,MainActivity::class.java)
                                    intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", firebaseUser?.uid)
                                    intent.putExtra("email_id",email)
                                    startActivity(intent)
                                    finish()
                                }
                                else{
                                    Toast.makeText(this@RegisterActivity,task.exception?.message.toString(),Toast.LENGTH_SHORT).show()
                                }

                            }



                }
            }

        }

    }
}