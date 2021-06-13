package com.rishav.messagepractice

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.rishav.messagepractice.Fragment.ChatsFragment
import com.rishav.messagepractice.Fragment.ProfileFragment
import com.rishav.messagepractice.Fragment.SearchFragment
import com.rishav.messagepractice.ModelClass.Users
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {

    private var firebaseuser:FirebaseUser?=null
    private var refuser:DatabaseReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar:Toolbar=findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title=""


        firebaseuser=FirebaseAuth.getInstance().currentUser
        refuser=FirebaseDatabase.getInstance().reference.child("users").child(firebaseuser!!.uid)

        val viewpager: ViewPager =findViewById(R.id.main_viewpager)
        val tabbar: TabLayout=findViewById(R.id.main_tab)
        val viewpageradapter=Fragmentpageradapter(supportFragmentManager)

        viewpageradapter.addFragment(ChatsFragment(),"Chats")
        viewpageradapter.addFragment(SearchFragment(),"Search")
        viewpageradapter.addFragment(ProfileFragment(),"Profile")

        viewpager.adapter=viewpageradapter
        tabbar.setupWithViewPager(viewpager)


        refuser!!.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val user: Users? =snapshot.getValue(Users::class.java)
                    val picture:CircleImageView=findViewById(R.id.main_profile_pic)
                    val username: TextView =findViewById(R.id.main_username)
                    username.text=user!!.getusername()
                    Picasso.get().load(user!!.getprofile()).error(R.drawable.prof)
                        .placeholder(R.drawable.prof).into(picture)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this,WelcomeActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    internal class Fragmentpageradapter(fragmentmanager:FragmentManager)
        :FragmentPagerAdapter(fragmentmanager)
    {

        private val fragments:ArrayList<Fragment>
        private val titles:ArrayList<String>
        init{
            fragments=ArrayList<Fragment>()
            titles=ArrayList<String>()
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        fun addFragment(fragment:Fragment,title:String)
        {
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}