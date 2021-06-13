package com.rishav.messagepractice

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(){
    private var ref: FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        val toolbar:Toolbar=findViewById(R.id.login_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title="Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this,WelcomeActivity::class.java))
            finish()
        }
        ref= FirebaseAuth.getInstance()

        val login: Button =findViewById(R.id.login_btn)

        login.setOnClickListener{login_activity()}
    }
    private fun login_activity()
    {
        val email: EditText =findViewById(R.id.login_email)
        val pass:EditText=findViewById(R.id.login_password)

        val emailid:String=email.text.toString()
        val password:String=pass.text.toString()

        if(emailid=="")
        {
            Toast.makeText(this,"enter email id",Toast.LENGTH_SHORT).show()
            val alert=AlertDialog.Builder(this)
            alert.setTitle("Email")
            alert.setIcon(android.R.drawable.ic_dialog_alert)
            alert.setMessage("Please enter email id")
            alert.setPositiveButton("OK"){
                a,b->
            }
            alert.setCancelable(false)
            alert.create()
            alert.show()
        }
        else if(password=="")
        {
            Toast.makeText(this,"enter email id",Toast.LENGTH_SHORT).show()
            val alert=AlertDialog.Builder(this)
            alert.setTitle("Email")
            alert.setIcon(android.R.drawable.ic_dialog_alert)
            alert.setMessage("Please enter email id")
            alert.setPositiveButton("OK"){
                a,b->
            }
            alert.setCancelable(false)
            alert.create()
            alert.show()
        }
        else
        {
            ref!!.signInWithEmailAndPassword(emailid,password)
                    .addOnCompleteListener { task->
                        if(task.isSuccessful)
                        {
                            Toast.makeText(this,"Logging in....",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        }
                        else
                        {
                            Toast.makeText(this,"cannot login",Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }
}