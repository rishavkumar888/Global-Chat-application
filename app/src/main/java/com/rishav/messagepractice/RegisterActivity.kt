package com.rishav.messagepractice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity:AppCompatActivity() {


    private lateinit var refuser:DatabaseReference
    private lateinit var mauth:FirebaseAuth
    private var firebaseuserid:String=""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.acticity_register)
        val toolbar:Toolbar=findViewById(R.id.register_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title="Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent= Intent(this,WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mauth=FirebaseAuth.getInstance()
        val regis: Button =findViewById(R.id.register_btn)
        regis.setOnClickListener{register()}
    }
    fun register()
    {
        val user:EditText=findViewById(R.id.register_user)
        val email:EditText=findViewById(R.id.register_email)
        val pass:EditText=findViewById(R.id.register_pass)

        var username=user.text.toString()
        var emailid=email.text.toString()
        var password=pass.text.toString()

        if(username.isEmpty())
        {
            Toast.makeText(this,"enter username",Toast.LENGTH_SHORT).show()
            val alert=AlertDialog.Builder(this)
            alert.setTitle("Username")
            alert.setIcon(android.R.drawable.ic_dialog_alert)
            alert.setMessage("enter a username")
            alert.setPositiveButton("Ok")
            {
                a,b->
            }
            alert.create().setCancelable(false)
            alert.show()
        }
        else if(emailid.isEmpty())
        {
            Toast.makeText(this,"enter emailid",Toast.LENGTH_SHORT).show()
            val alert=AlertDialog.Builder(this)
            alert.setTitle("Email id")
            alert.setIcon(android.R.drawable.ic_dialog_alert)
            alert.setMessage("enter a email id")
            alert.setPositiveButton("Ok")
            {
                    a,b->
            }
            alert.create().setCancelable(false)
            alert.show()
        }
        else if(password.isEmpty())
        {
            Toast.makeText(this,"enter password",Toast.LENGTH_SHORT).show()
            val alert=AlertDialog.Builder(this)
            alert.setTitle("Password")
            alert.setIcon(android.R.drawable.ic_dialog_alert)
            alert.setMessage("enter a password")
            alert.setPositiveButton("Ok")
            {
                    a,b->
            }
            alert.create().setCancelable(false)
            alert.show()
        }
        else if(password.length<6)
        {
            Toast.makeText(this,"password length to be greater than 6",Toast.LENGTH_SHORT).show()
            val alert=AlertDialog.Builder(this)
            alert.setTitle("Password")
            alert.setIcon(android.R.drawable.ic_dialog_alert)
            alert.setMessage("enter a password of length greater than 5")
            alert.setPositiveButton("Ok")
            {
                    a,b->
            }
            alert.create().setCancelable(false)
            alert.show()
        }
        else
        {
            mauth.createUserWithEmailAndPassword(emailid,password)
                .addOnCompleteListener { task->
                    if(task.isSuccessful)
                    {
                        firebaseuserid=mauth.currentUser!!.uid
                        refuser= FirebaseDatabase.getInstance().reference.child("users").child(firebaseuserid)

                        val hm=HashMap<String,Any>()
                        hm["uid"]=firebaseuserid
                        hm["username"]=username
                        hm["search"]=username.toLowerCase()
                        hm["profile"]="https://firebasestorage.googleapis.com/v0/b/messagepractice-7b768.appspot.com/o/profile.jpg?alt=media&token=c3beb24c-2f50-4e60-bc44-54aa25011e10"
                        hm["cover"]="https://firebasestorage.googleapis.com/v0/b/messagepractice-7b768.appspot.com/o/download%20(1).jfif?alt=media&token=eb919b4b-4f75-48b8-a245-27ee9df3a283"
                        hm["facebook"]="www.facebook.com"
                        hm["instagram"]="www.insta.com"
                        hm["website"]="www.google.com"
                        hm["status"]="chillin..."
                        hm["description"]="i am a human"

                        refuser.updateChildren(hm)
                            .addOnCompleteListener{
                                startActivity(Intent(this,MainActivity::class.java))
                                finish()
                            }
                    }
                    else
                    {
                        Log.d("m","cannot register, Please try again")
                        val alert=AlertDialog.Builder(this)
                        alert.setTitle("Registeration Error")
                        alert.setIcon(android.R.drawable.ic_dialog_alert)
                        alert.setMessage("an error occured while registering. Please try again")
                        alert.setPositiveButton("Ok")
                        {
                                a,b->
                        }
                        alert.create().setCancelable(false)
                        alert.show()
                    }
                }
        }
    }
}