package com.rishav.messagepractice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.rishav.messagepractice.Adapter.ChatAdapter
import com.rishav.messagepractice.ModelClass.Chat
import com.rishav.messagepractice.ModelClass.Users
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class ChatActivity: AppCompatActivity() {

    private val firebaseuser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private val user: Users?=null
    private var reference: DatabaseReference?=null
    var messagingToUserId:String=""
    private lateinit var recycler: RecyclerView
    private var chatAdapter: ChatAdapter?=null
    var mchat:List<Chat>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)
        val toolbar: Toolbar =findViewById(R.id.chat_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title=""
        toolbar.setNavigationOnClickListener { task->
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        val profile: CircleImageView =findViewById(R.id.chat_profile)
        val username: TextView =findViewById(R.id.chat_username)
        recycler=findViewById(R.id.chat_recycler_view)
//        val imagebtn: ImageView =findViewById(R.id.chat_action_image_btn)
        val textplace: EditText =findViewById(R.id.chat_action_text)
        val sendbtn:ImageView=findViewById(R.id.chat_action_send)


        recycler.setHasFixedSize(true)
        var linear= LinearLayoutManager(applicationContext)
        linear.stackFromEnd=true
        recycler.layoutManager=linear

        intent=intent
        messagingToUserId= intent.getStringExtra("visit_id").toString()

        reference= FirebaseDatabase.getInstance().reference
                .child("users").child(messagingToUserId)
        reference!!.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user:Users?=snapshot.getValue(Users::class.java)

                Picasso.get().load(user!!.getprofile()).error(R.drawable.prof).placeholder(R.drawable.prof)
                        .into(profile)
                username.text=user.getusername()

                retrieveMessage(firebaseuser!!.uid,messagingToUserId)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        sendbtn.setOnClickListener{
            val message=textplace.text.toString()
            if(message=="")
            {
                Toast.makeText(this,"Type something",Toast.LENGTH_SHORT).show()
            }
            else
            {
                sendmessage(firebaseuser.uid!!,messagingToUserId,message)
            }
            textplace.setText("")
        }
    }
    private fun retrieveMessage(sender:String,receiver:String)
    {
        mchat=ArrayList()
        reference=FirebaseDatabase.getInstance().reference.child("chat")
        reference!!.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (mchat as ArrayList<Chat>).clear()

                for(sn in snapshot.children)
                {
                    val chat=sn.getValue(Chat::class.java)
                    if(chat!!.getsender() == receiver && chat.getreceiver() == sender)
                    {
                        chat.setisseen(true)
                        (mchat as ArrayList<Chat>).add(chat)
                    }
                    else if(chat!!.getreceiver() == receiver && chat.getsender() == sender)
                    {
//                        chat.setisseen(true)
                        (mchat as ArrayList<Chat>).add(chat)
                    }
                }
//                Log.d("m","[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[going to chat adapterr")
                chatAdapter= ChatAdapter(this@ChatActivity,(mchat as ArrayList<Chat>))
                recycler.adapter=chatAdapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun sendmessage(sender:String,receiver:String,message:String)
    {
//        Log.d("m","[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[sending message")
        val reference=FirebaseDatabase.getInstance().reference
        val messagekey=reference.push().key
        val hm=HashMap<String,Any?>()
        hm["sender"]=sender
        hm["receiver"]=receiver
        hm["message"]=message
        hm["messageid"]=messagekey
        hm["isseen"]=false

        reference.child("chat").child(messagekey!!).setValue(hm)
                .addOnCompleteListener{ task->
                    if(task.isSuccessful)
                    {
                        val chatlistreference=FirebaseDatabase.getInstance().reference
                                .child("chatList")
                                .child(sender)
                                .child(receiver)

                        chatlistreference.addListenerForSingleValueEvent(object:ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(!snapshot.exists())
                                {
                                    chatlistreference.child("id").setValue(receiver)
                                }
                                val chatlistreceiverref=FirebaseDatabase.getInstance().reference
                                        .child("chatList")
                                        .child(receiver)
                                        .child(sender)
                                chatlistreceiverref.child("id").setValue(sender)
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                        chatlistreference.child("id").setValue(sender)
                    }
                }


//        reference.child("chat").child(messagekey!!).setValue(hm)
//                .addOnCompleteListener{
//                    task->
//                    if(task.isSuccessful)
//                    {
//                        FirebaseDatabase.getInstance().reference.child("chatList")
//                                .child(sender).child(receiver).child("id").setValue(receiver)
//                        FirebaseDatabase.getInstance().reference.child("chatList")
//                                .child(receiver).child(sender).child("id").setValue(sender)
//                    }
//                }
    }
}