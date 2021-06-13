package com.rishav.messagepractice.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ValueEventListener
import com.rishav.messagepractice.ModelClass.Chat
import com.rishav.messagepractice.R


class chatViewHolder(v: View): RecyclerView.ViewHolder(v){
    val chattext:TextView=v.findViewById(R.id.chat_text)
    val chatseen:TextView=v.findViewById(R.id.chat_seen)
}

class ChatAdapter(context: Context, mchat:List<Chat>)
    :RecyclerView.Adapter<chatViewHolder?>(){

    private val context: Context
    private val mchat:List<Chat>
    init{
        this.context=context
        this.mchat=mchat
    }



    var firebaseuser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): chatViewHolder {

//        Log.d("m","[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[entered chat adapter on create")
        if(position==1)
        {
            val view:View= LayoutInflater.from(context).inflate(R.layout.activity_chat_right,parent,false)
            return chatViewHolder(view)
        }
        else
        {
            val view:View=LayoutInflater.from(context).inflate(R.layout.activity_chat_left,parent,false)
            return chatViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: chatViewHolder, position: Int) {

//        Log.d("m","[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[entered chat adapter on bind")

        val chat:Chat=mchat[position]
        holder.chattext.text=chat.getmessage()
        if(position==mchat.size-1)
        {
            if(chat.getisseen()==true)
            {
                holder.chatseen.text="Seen"
            }
            else
            {
                holder.chatseen.text="Sent"
            }
        }
        else
        {
            holder.chatseen.visibility=View.GONE
        }
    }

    override fun getItemCount(): Int {
//        Log.d("m","[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[entered chat adapter on count")
        return if(mchat.isNotEmpty()) mchat.size else 0
    }

    override fun getItemViewType(position: Int): Int {
//        Log.d("m","[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[entered chat adapter on getitemview type")
        return if(mchat[position].getsender()==(firebaseuser!!.uid))
        {
            1
        }
        else
        {
            0
        }
    }
}