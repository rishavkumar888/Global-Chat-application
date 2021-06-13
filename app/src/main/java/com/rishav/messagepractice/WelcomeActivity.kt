package com.rishav.messagepractice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class WelcomeActivity: AppCompatActivity() {

    var firebaseuser: FirebaseUser?=null
    override fun onCreate(savedInstanceInstance: Bundle?)
    {
        super.onCreate(savedInstanceInstance)
        setContentView(R.layout.welcome)

        val regis:Button=findViewById(R.id.welcome_register)
        val login:Button=findViewById(R.id.welcome_login)

        regis.setOnClickListener{
            val intent= Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        login.setOnClickListener{
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseuser= FirebaseAuth.getInstance().currentUser
        if(firebaseuser!=null)
        {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}