package com.rishav.messagepractice.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rishav.messagepractice.Adapter.ChatAdapter
import com.rishav.messagepractice.Adapter.UserAdapter
import com.rishav.messagepractice.ModelClass.Users
import com.rishav.messagepractice.ModelClass.chat_list
import com.rishav.messagepractice.R
import java.util.stream.Collectors

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ChatsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerview: RecyclerView
    private val firebaseuser= FirebaseAuth.getInstance().currentUser
    private var mchatlist:List<chat_list>?=null
    private lateinit var chatAdapter:UserAdapter
    private var musers:List<Users>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val view=inflater.inflate(R.layout.fragment_chats, container, false)

        recyclerview=view.findViewById(R.id.main_chat_recycler_view)
        recyclerview.layoutManager=LinearLayoutManager(context)
        recyclerview.setHasFixedSize(true)

        mchatlist=ArrayList()
        musers=ArrayList()

        val ref=FirebaseDatabase.getInstance().reference.child("chatList").child(firebaseuser!!.uid)
        ref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                (mchatlist as ArrayList).clear()
                for(sn in snapshot.children)
                {
                    val chatlist:chat_list?=sn.getValue(chat_list::class.java)
                    (mchatlist as ArrayList).add(chatlist!!)
                }
                retrievechatlist()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ChatsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun retrievechatlist()
    {
        val ref=FirebaseDatabase.getInstance().reference.child("users")

        ref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                (musers as ArrayList).clear()

                for(sn in snapshot.children)
                {
                    val user:Users?=sn.getValue(Users::class.java)

                    for(ch in mchatlist!!)
                    {
                        if(user!!.getuid().equals(ch.getid()))
                        {
                            (musers as ArrayList).add(user)
                        }
                    }
                }
//                musers=musers!!.distinct()
                if(context!=null) {
                    if(musers!!.isEmpty())
                    {
                        var temp=ArrayList<Users>()
                        chatAdapter=UserAdapter(context!!,temp)
                    }
                    else
                    {
                        chatAdapter = UserAdapter(context!!, musers!!)
                    }
                    recyclerview.adapter=chatAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}