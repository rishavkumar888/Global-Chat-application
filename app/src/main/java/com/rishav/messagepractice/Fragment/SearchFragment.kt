package com.rishav.messagepractice.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rishav.messagepractice.Adapter.UserAdapter
import com.rishav.messagepractice.ModelClass.Users
import com.rishav.messagepractice.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class SearchFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var search: EditText
    private lateinit var musers:List<Users>
    private lateinit var recyclerview:RecyclerView
    private var useradapter:UserAdapter?=null

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
        val view=inflater.inflate(R.layout.fragment_search, container, false)

        search=view.findViewById(R.id.main_search)
        recyclerview=view.findViewById(R.id.main_search_recyclerview)

        recyclerview.layoutManager=LinearLayoutManager(context)
        recyclerview.setHasFixedSize(true)

        musers=ArrayList()
        retrieveAllUsers()

        search.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int){
                searchForUsers(s.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SearchFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun retrieveAllUsers()
    {
        val firebaseuserId= FirebaseAuth.getInstance().currentUser!!.uid
        val refuser= FirebaseDatabase.getInstance().reference.child("users")
        refuser.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                (musers as ArrayList<Users>).clear()
                if(search.text.toString()=="")
                {
                    for(sn in snapshot.children)
                    {
                        val user: Users? =sn.getValue(Users::class.java)
                        if(!user!!.getuid().equals(firebaseuserId))
                        {
                            (musers as ArrayList).add(user)
                        }
                    }
                }
                if(context!=null)
                {
                    useradapter= UserAdapter(context!!,musers)
                }
                recyclerview.adapter=useradapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun searchForUsers(str:String)
    {
        val firebaseUserId=FirebaseAuth.getInstance().currentUser!!.uid
        val querry=FirebaseDatabase.getInstance().reference.child("users")
                .orderByChild("search").startAt(str).endAt(str+"utf8ff")

        querry.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (musers as ArrayList<Users>).clear()
                for(sn in snapshot.children)
                {
                    val user:Users?=sn.getValue(Users::class.java)
                    if(!user!!.getuid().equals(firebaseUserId))
                    {
                        (musers as ArrayList).add(user)
                    }
                }
                if(context!=null)
                {
                    useradapter=UserAdapter(context!!,musers)
                }
                recyclerview.adapter=useradapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}