package com.rishav.messagepractice.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rishav.messagepractice.ChatActivity
import com.rishav.messagepractice.ModelClass.Users
import com.rishav.messagepractice.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class userRecyclerHolder(v: View):RecyclerView.ViewHolder(v)
{
    val profile:CircleImageView=v.findViewById(R.id.main_search_profileimage)
    val user:TextView=v.findViewById(R.id.main_search_username)
    val status:TextView=v.findViewById(R.id.main_search_status)
}

class UserAdapter(private val context:Context,private val mUsers:List<Users>)
    : RecyclerView.Adapter<userRecyclerHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userRecyclerHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.usersearch,parent,false)
        return userRecyclerHolder(view)
    }

    override fun onBindViewHolder(holder: userRecyclerHolder, position: Int) {
        val user:Users=mUsers[position]

        Picasso.get().load(user.getprofile()).error(R.drawable.profile).placeholder(R.drawable.profile)
                .into(holder.profile)
        holder.user.text=user.getusername()
        holder.status.text=user.getstatus()

        holder.itemView.setOnClickListener{
            val options=arrayOf<CharSequence>(
                "Message"
            )

            val alert=AlertDialog.Builder(context)
            alert.setTitle("Select Action")
            alert.setItems(options, DialogInterface.OnClickListener{
                dialog,which->
                if(which==0)
                {
                    val intent= Intent(context, ChatActivity::class.java)
                    intent.putExtra("visit_id",user.getuid())
                    if(context!=null) {
                        context.startActivity(intent)
                    }
                }
            })
            alert.show()
        }
    }

    override fun getItemCount(): Int {
        return if(mUsers.isEmpty()) 0 else mUsers.size
    }
}