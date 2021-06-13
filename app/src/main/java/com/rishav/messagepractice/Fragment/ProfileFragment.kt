package com.rishav.messagepractice.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.rishav.messagepractice.ModelClass.Users
import com.rishav.messagepractice.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var cover: ImageView
    private lateinit var profile:CircleImageView
    private lateinit var username:TextView
    private lateinit var status:EditText
    private lateinit var discription:EditText

    private lateinit var facebook:CircleImageView
    private lateinit var insta:CircleImageView
    private lateinit var website:CircleImageView

    private lateinit var ref:DatabaseReference
    private lateinit var firebaseuser: FirebaseUser
    private lateinit var storageref:StorageReference

    var socialChecker:String=""
    var coverChecker:String=""
    var messageChecker:String=""
    val RequestCode=438

    var imageuri:Uri?=null

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
        val view=inflater.inflate(R.layout.fragment_profile, container, false)

        cover=view.findViewById(R.id.main_profile_cover)
        profile=view.findViewById(R.id.main_profile_profile)
        username=view.findViewById(R.id.main_profile_username)
        status=view.findViewById(R.id.main_profile_status)
        discription=view.findViewById(R.id.main_profile_describe)

        facebook=view.findViewById(R.id.main_profile_fb)
        insta=view.findViewById(R.id.main_profile_insta)
        website=view.findViewById(R.id.main_profile_website)

        firebaseuser= FirebaseAuth.getInstance().currentUser!!
        ref= FirebaseDatabase.getInstance().reference.child("users").child(firebaseuser.uid)
        storageref=FirebaseStorage.getInstance().reference.child("user_image")


        ref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val user: Users?=snapshot.getValue(Users::class.java)
                    username.text=user!!.getusername()
                    Picasso.get().load(user.getcover()).error(R.drawable.cover).placeholder(R.drawable.cover)
                            .into(cover)
                    Picasso.get().load(user.getprofile()).error(R.drawable.prof).placeholder(R.drawable.prof)
                            .into(profile)
                    discription.setText(user.getdescription())
                    status.setText(user.getstatus())
                }
            }
            override fun onCancelled(error:DatabaseError)
            {
            }
        })

        status.setOnClickListener{
            messageChecker="status"
            update(status.text.toString())
        }

        discription.setOnClickListener{
            messageChecker="description"
            update(discription.text.toString())
        }

        facebook.setOnClickListener{
            socialChecker="facebook"
            changeLink()
        }
        insta.setOnClickListener{
            socialChecker="insta"
            changeLink()
        }
        website.setOnClickListener{
            socialChecker="website"
            changeLink()
        }

        cover.setOnClickListener{
            coverChecker="cover"
            changeImage()
        }

        profile.setOnClickListener{
            coverChecker="profile"
            changeImage()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ProfileFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun changeLink()
    {
        val builder=AlertDialog.Builder(context)
        val edittext=EditText(context)

        if(socialChecker=="facebook")
        {
            builder.setTitle("Change Facebook Link")
            edittext.setHint("www.facebook.com")
        }
        else if(socialChecker=="insta")
        {
            builder.setTitle("Change Insta Link")
            edittext.setHint("www.instagram.com")
        }
        else if(socialChecker=="website")
        {
            builder.setTitle("Change Website Link")
            edittext.setHint("www.google.com")
        }

        builder.setView(edittext)
        builder.setPositiveButton("Create",DialogInterface.OnClickListener{
            _,_->
            val str=edittext.text.toString()
            if(str=="")
            {
                Toast.makeText(context,"Enter valid link",Toast.LENGTH_LONG).show()
            }
            else
            {
                createLink(str)
            }
        })
        builder.setNegativeButton("cancel",DialogInterface.OnClickListener{
            dialog,_->
            dialog.cancel()
        })
        builder.show()
    }

    private fun createLink(str:String)
    {
        var hm=HashMap<String,Any>()
        if(socialChecker=="facebook")
        {
            hm["facebook"]=str
        }
        else if(socialChecker=="insta")
        {
            hm["instagram"]=str
        }
        else if(socialChecker=="website")
        {
            hm["website"]=str
        }

        ref.updateChildren(hm).addOnCompleteListener{
            task->
            if(task.isSuccessful)
            {
                Toast.makeText(context,"Link changed",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeImage()
    {
        val intent= Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,RequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==RequestCode&&resultCode== Activity.RESULT_OK&&data!!.data!=null)
        {
            imageuri=data.data
            uploadImage()
        }
    }

    private fun uploadImage()
    {
        val message=ProgressDialog(context)
        message.setMessage("Uploading Image")
        message.show()

        if(imageuri!=null)
        {
            val fileref=storageref!!.child(System.currentTimeMillis().toString()+".jpg")
            var uploadTask: StorageTask<*>
            uploadTask=fileref.putFile(imageuri!!)
            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot,  Task <Uri>>{ task->

                if(!task.isSuccessful)
                {
                    task.exception?.let{
                        throw it
                    }
                }
                return@Continuation fileref.downloadUrl
            }).addOnCompleteListener{task->
                if(task.isSuccessful)
                {
                    val downloadUrl=task.result
                    val url=downloadUrl.toString()
                    var hm=HashMap<String,Any>()

                    if(coverChecker=="cover")
                    {
                        hm["cover"]=url
                    }
                    else if(coverChecker=="profile")
                    {
                        hm["profile"]=url
                    }

                    ref.updateChildren(hm)
                    message.dismiss()
                }
            }
        }
    }
    private fun update(str:String)
    {

        val builder=AlertDialog.Builder(context)
        builder.setTitle("change $messageChecker")
        val edittext=EditText(context)
        edittext.setText(str)
        builder.setView(edittext)

        builder.setPositiveButton("Update",DialogInterface.OnClickListener{
            _,_->
            if(str=="")
            {
                updateMessage("Chillin...")
            }
            else
            {
                updateMessage(edittext.text.toString())
            }
        })
        builder.setNegativeButton("Cancel",DialogInterface.OnClickListener{
            dialog,_->
            dialog.cancel()
        })
        builder.show()
    }

    private fun updateMessage(str:String)
    {
        var hm=HashMap<String,Any>()
        hm[messageChecker]=str
        val refuser=FirebaseDatabase.getInstance().reference.child("users")
                .child(firebaseuser.uid)
        refuser.updateChildren(hm)
        Toast.makeText(context,"$messageChecker updated",Toast.LENGTH_LONG).show()
    }
}